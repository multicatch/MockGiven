package xyz.multicatch.mockgiven.core.scenario.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.tngtech.jgiven.annotation.*;
import com.tngtech.jgiven.attachment.Attachment;
import com.tngtech.jgiven.config.AbstractJGivenConfiguration;
import com.tngtech.jgiven.config.ConfigurationUtil;
import com.tngtech.jgiven.config.DefaultConfiguration;
import com.tngtech.jgiven.config.TagConfiguration;
import com.tngtech.jgiven.exception.JGivenWrongUsageException;
import com.tngtech.jgiven.format.ObjectFormatter;
import com.tngtech.jgiven.impl.Config;
import com.tngtech.jgiven.impl.ScenarioModelBuilder;
import com.tngtech.jgiven.impl.util.AssertionUtil;
import com.tngtech.jgiven.impl.util.WordUtil;
import com.tngtech.jgiven.report.model.*;
import xyz.multicatch.mockgiven.core.annotations.as.AsProviderFactory;
import xyz.multicatch.mockgiven.core.annotations.caseas.CaseAsFactory;
import xyz.multicatch.mockgiven.core.annotations.caseas.CaseAsProviderFactory;
import xyz.multicatch.mockgiven.core.annotations.description.AnnotatedDescriptionFactory;
import xyz.multicatch.mockgiven.core.annotations.description.DescriptionData;
import xyz.multicatch.mockgiven.core.annotations.description.DescriptionQueue;
import xyz.multicatch.mockgiven.core.annotations.description.InlineWithNext;
import xyz.multicatch.mockgiven.core.annotations.tag.AnnotationTagExtractor;
import xyz.multicatch.mockgiven.core.annotations.tag.AnnotationTagUtils;
import xyz.multicatch.mockgiven.core.resources.TextResourceProvider;
import xyz.multicatch.mockgiven.core.scenario.cases.CaseDescription;
import xyz.multicatch.mockgiven.core.scenario.cases.CaseDescriptionFactory;
import xyz.multicatch.mockgiven.core.scenario.cases.ExtendedScenarioCaseModel;
import xyz.multicatch.mockgiven.core.scenario.methods.DescriptionFactory;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ArgumentUtils;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ParameterFormatterFactory;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ParameterFormatterUtils;
import xyz.multicatch.mockgiven.core.scenario.state.CurrentScenarioState;
import xyz.multicatch.mockgiven.core.scenario.steps.ExtendedStepModel;
import xyz.multicatch.mockgiven.core.scenario.steps.StepCommentFactory;
import xyz.multicatch.mockgiven.core.scenario.steps.StepModelFactory;
import xyz.multicatch.mockgiven.core.utils.ExceptionUtils;

public class MockScenarioModelBuilder extends ScenarioModelBuilder {
    private static final Set<String> STACK_TRACE_FILTER = ImmutableSet
            .of("sun.reflect", "com.tngtech.jgiven.impl.intercept", "com.tngtech.jgiven.impl.intercept", "$$EnhancerByCGLIB$$",
                    "java.lang.reflect", "net.sf.cglib.proxy", "com.sun.proxy");
    private static final boolean FILTER_STACK_TRACE = Config.config()
                                                            .filterStackTrace();

    private final Stack<ExtendedStepModel> parentSteps = new Stack<>();
    private final CurrentScenarioState currentScenarioState;
    private final StepCommentFactory stepCommentFactory;
    private final DescriptionFactory descriptionFactory;
    private final CaseDescriptionFactory caseDescriptionFactory;
    private final DescriptionQueue descriptionQueue;

    private AbstractJGivenConfiguration configuration;
    private StepModelFactory stepModelFactory;
    private AnnotationTagExtractor annotationTagExtractor;
    private ParameterFormatterFactory formatterFactory;

    private ExtendedScenarioModel scenarioModel;
    private ExtendedScenarioCaseModel scenarioCaseModel;
    private ExtendedStepModel currentStep;
    private Word introWord;
    private long scenarioStartedNanos;
    private ReportModel reportModel;

    public MockScenarioModelBuilder(CurrentScenarioState currentScenarioState, TextResourceProvider textResourceProvider) {
        this.currentScenarioState = currentScenarioState;
        this.stepCommentFactory = new StepCommentFactory();
        this.descriptionFactory = new DescriptionFactory(new AsProviderFactory(), new AnnotatedDescriptionFactory(), textResourceProvider);
        this.caseDescriptionFactory = new CaseDescriptionFactory(new CaseAsFactory(), new CaseAsProviderFactory());
        this.descriptionQueue = new DescriptionQueue();
        this.configuration = new DefaultConfiguration();
        initializeDependentOnConfiguration();
    }

    public MockScenarioModelBuilder(
            CurrentScenarioState currentScenarioState,
            StepCommentFactory stepCommentFactory,
            DescriptionFactory descriptionFactory,
            CaseDescriptionFactory caseDescriptionFactory,
            DescriptionQueue descriptionQueue
    ) {
        this.currentScenarioState = currentScenarioState;
        this.stepCommentFactory = stepCommentFactory;
        this.descriptionFactory = descriptionFactory;
        this.caseDescriptionFactory = caseDescriptionFactory;
        this.descriptionQueue = descriptionQueue;
        this.configuration = new DefaultConfiguration();
        initializeDependentOnConfiguration();
    }

    private void initializeDependentOnConfiguration() {
        formatterFactory = new ParameterFormatterFactory(configuration);
        stepModelFactory = new StepModelFactory(currentScenarioState, formatterFactory, descriptionFactory);
        annotationTagExtractor = AnnotationTagExtractor.forConfig(configuration);
    }

    @Override
    public void scenarioStarted(String description) {
        scenarioStartedNanos = System.nanoTime();
        String readableDescription = description;

        if (description.contains("_")) {
            readableDescription = description.replace('_', ' ');
        } else if (!description.contains(" ")) {
            readableDescription = WordUtil.camelCaseToCapitalizedReadableText(description);
        }

        scenarioCaseModel = new ExtendedScenarioCaseModel();

        scenarioModel = new ExtendedScenarioModel();
        scenarioModel.addCase(scenarioCaseModel);
        scenarioModel.setDescription(readableDescription);
    }

    @Override
    public void addStepMethod(
            Method paramMethod,
            List<NamedArgument> arguments,
            InvocationMode mode,
            boolean hasNestedSteps
    ) {
        ExtendedStepModel stepModel = stepModelFactory.create(paramMethod, arguments, mode, introWord);
        DescriptionData description = DescriptionData.of(stepModel);
        descriptionQueue.add(description);

        if (introWord != null) {
            introWord = null;
        }

        if (!paramMethod.isAnnotationPresent(InlineWithNext.class)) {
            stepModel.setDescription(descriptionQueue.join());

            if (parentSteps.empty()) {
                getCurrentScenarioCase().addStep(stepModel);
            } else {
                parentSteps.peek()
                           .addNestedStep(stepModel);
            }

            if (hasNestedSteps) {
                parentSteps.push(stepModel);
            }

            currentStep = stepModel;
        }
    }

    @Override
    public void introWordAdded(String value) {
        introWord = new Word();
        introWord.setIntroWord(true);
        introWord.setValue(value);
    }

    @Override
    public void stepCommentAdded(List<NamedArgument> arguments) {
        if (currentStep == null) {
            throw new JGivenWrongUsageException("A step comment must be added after the corresponding step, "
                    + "but no step has been executed yet.");
        }

        currentStep.setComment(stepCommentFactory.create(arguments));
    }

    private ScenarioCaseModel getCurrentScenarioCase() {
        if (scenarioCaseModel == null) {
            scenarioStarted("A Scenario");
        }
        return scenarioCaseModel;
    }

    @Override
    public void stepMethodInvoked(
            Method method,
            List<NamedArgument> arguments,
            InvocationMode mode,
            boolean hasNestedSteps
    ) {
        if (method.isAnnotationPresent(IntroWord.class)) {
            introWordAdded(descriptionFactory.create(currentScenarioState.getCurrentStage(), method));
        } else if (method.isAnnotationPresent(StepComment.class)) {
            stepCommentAdded(arguments);
        } else {
            addTags(method.getAnnotations());
            addTags(method.getDeclaringClass()
                          .getAnnotations());

            addStepMethod(method, arguments, mode, hasNestedSteps);
        }
    }

    @Override
    public void stepMethodFailed(Throwable t) {
        if (currentStep != null) {
            currentStep.setStatus(StepStatus.FAILED);
        }
    }

    @Override
    public void stepMethodFinished(
            long durationInNanos,
            boolean hasNestedSteps
    ) {
        if (hasNestedSteps && !parentSteps.isEmpty()) {
            currentStep = parentSteps.peek();
        }

        if (currentStep != null) {
            currentStep.setDurationInNanos(durationInNanos);
            if (hasNestedSteps) {
                if (currentStep.getStatus() != StepStatus.FAILED) {
                    currentStep.inheritStatusFromNested();
                }
                parentSteps.pop();
            }
        }

        if (!hasNestedSteps && !parentSteps.isEmpty()) {
            currentStep = parentSteps.peek();
        }
    }

    @Override
    public void scenarioFailed(Throwable e) {
        scenarioCaseModel.setException(e, getStackTrace(e));
    }

    private List<String> getStackTrace(Throwable throwable) {
        if (FILTER_STACK_TRACE) {
            return ExceptionUtils.getFilteredStackTrace(throwable, STACK_TRACE_FILTER);
        } else {
            return ExceptionUtils.getStackTrace(throwable);
        }
    }

    @Override
    public void scenarioStarted(
            Class<?> testClass,
            Method method,
            List<NamedArgument> namedArguments
    ) {
        readConfiguration(testClass);
        readAnnotations(testClass, method);

        scenarioModel.setClassName(testClass.getName());
        scenarioModel.setExplicitParametersWithoutUnderline(ArgumentUtils.getNames(namedArguments));
        scenarioModel.setTestMethodName(method.getName());

        List<ObjectFormatter<?>> formatter = formatterFactory.create(method.getParameters(), namedArguments);
        List<String> arguments = ParameterFormatterUtils.toStringList(formatter, ArgumentUtils.getValues(namedArguments));
        scenarioCaseModel.setExplicitArguments(arguments);

        setCaseDescription(testClass, method, namedArguments);
    }

    private void readConfiguration(Class<?> testClass) {
        configuration = ConfigurationUtil.getConfiguration(testClass);
        initializeDependentOnConfiguration();
    }

    private void readAnnotations(
            Class<?> testClass,
            Method method
    ) {
        String scenarioDescription = descriptionFactory.create(currentScenarioState.getCurrentStage(), method);
        scenarioStarted(scenarioDescription);

        if (method.isAnnotationPresent(ExtendedDescription.class)) {
            scenarioModel.setExtendedDescription(method.getAnnotation(ExtendedDescription.class)
                                                       .value());
        }

        if (method.isAnnotationPresent(NotImplementedYet.class) || method.isAnnotationPresent(Pending.class)) {
            scenarioCaseModel.setStatus(ExecutionStatus.SCENARIO_PENDING);
        }

        if (scenarioCaseModel.isFirstCase()) {
            addTags(testClass.getAnnotations());
            addTags(method.getAnnotations());
        }
    }

    private void setCaseDescription(
            Class<?> testClass,
            Method method,
            List<NamedArgument> namedArguments
    ) {

        CaseDescription caseDescription = caseDescriptionFactory.create(method, testClass, scenarioCaseModel, namedArguments);

        if (caseDescription != null) {
            String description = caseDescriptionFactory.create(caseDescription, scenarioCaseModel.getExplicitArguments());
            scenarioCaseModel.setDescription(description);
        }
    }

    public void addTags(Annotation... annotations) {
        for (Annotation annotation : annotations) {
            addTags(annotationTagExtractor.extract(annotation));
        }
    }

    private void addTags(List<Tag> tags) {
        if (!tags.isEmpty()) {
            if (reportModel != null) {
                this.reportModel.addTags(tags);
            }

            if (scenarioModel != null) {
                this.scenarioModel.addTags(tags);
            }
        }
    }

    @Override
    public void scenarioFinished() {
        AssertionUtil.assertTrue(scenarioStartedNanos > 0, "Scenario has no start time");
        long durationInNanos = System.nanoTime() - scenarioStartedNanos;
        scenarioCaseModel.setDurationInNanos(durationInNanos);
        scenarioModel.addDurationInNanos(durationInNanos);
        reportModel.addScenarioModelOrMergeWithExistingOne(scenarioModel);
    }

    @Override
    public void attachmentAdded(Attachment attachment) {
        currentStep.setAttachment(attachment);
    }

    @Override
    public void extendedDescriptionUpdated(String extendedDescription) {
        currentStep.setExtendedDescription(extendedDescription);
    }

    @Override
    public void sectionAdded(String sectionTitle) {
        StepModel stepModel = new StepModel();
        stepModel.setName(sectionTitle);
        stepModel.addWords(new Word(sectionTitle));
        stepModel.setIsSectionTitle(true);
        getCurrentScenarioCase().addStep(stepModel);
    }

    @Override
    public void tagAdded(
            Class<? extends Annotation> annotationClass,
            String... values
    ) {
        TagConfiguration tagConfig = annotationTagExtractor.toTagConfiguration(annotationClass);
        List<Tag> tags = AnnotationTagUtils.toTags(tagConfig, Optional.empty());

        if (!tags.isEmpty()) {
            if (values.length > 0) {
                addTags(AnnotationTagUtils.getExplodedTags(Iterables.getOnlyElement(tags), values, null, tagConfig));
            } else {
                addTags(tags);
            }
        }
    }

    public void setReportModel(ReportModel reportModel) {
        this.reportModel = reportModel;
    }

    public ReportModel getReportModel() {
        return reportModel;
    }

    public ScenarioModel getScenarioModel() {
        return scenarioModel;
    }

    public ScenarioCaseModel getScenarioCaseModel() {
        return scenarioCaseModel;
    }

}
