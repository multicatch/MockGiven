package xyz.multicatch.mockgiven.core.scenario.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
import com.tngtech.jgiven.impl.util.ReflectionUtil;
import com.tngtech.jgiven.impl.util.WordUtil;
import com.tngtech.jgiven.report.model.*;
import xyz.multicatch.mockgiven.core.annotations.as.AsProviderFactory;
import xyz.multicatch.mockgiven.core.annotations.description.AnnotatedDescriptionFactory;
import xyz.multicatch.mockgiven.core.annotations.tag.AnnotationTagExtractor;
import xyz.multicatch.mockgiven.core.annotations.tag.AnnotationTagUtils;
import xyz.multicatch.mockgiven.core.scenario.methods.DescriptionFactory;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ArgumentUtils;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ParameterFormatterFactory;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ParameterFormatterUtils;
import xyz.multicatch.mockgiven.core.scenario.state.CurrentScenarioState;
import xyz.multicatch.mockgiven.core.scenario.steps.StepCommentFactory;
import xyz.multicatch.mockgiven.core.scenario.steps.StepModelFactory;
import xyz.multicatch.mockgiven.core.utils.ObjectUtils;

public class MockScenarioModelBuilder extends ScenarioModelBuilder {
    private static final Set<String> STACK_TRACE_FILTER = ImmutableSet
            .of("sun.reflect", "com.tngtech.jgiven.impl.intercept", "com.tngtech.jgiven.impl.intercept", "$$EnhancerByCGLIB$$",
                    "java.lang.reflect", "net.sf.cglib.proxy", "com.sun.proxy");
    private static final boolean FILTER_STACK_TRACE = Config.config()
                                                            .filterStackTrace();

    private final Stack<StepModel> parentSteps = new Stack<>();
    private final CurrentScenarioState currentScenarioState;
    private final StepCommentFactory stepCommentFactory;
    private final DescriptionFactory descriptionFactory;

    private AbstractJGivenConfiguration configuration;
    private StepModelFactory stepModelFactory;
    private AnnotationTagExtractor annotationTagExtractor;
    private ParameterFormatterFactory formatterFactory;

    private ScenarioModel scenarioModel;
    private ScenarioCaseModel scenarioCaseModel;
    private StepModel currentStep;
    private Word introWord;
    private long scenarioStartedNanos;
    private ReportModel reportModel;

    public MockScenarioModelBuilder(CurrentScenarioState currentScenarioState) {
        this.currentScenarioState = currentScenarioState;
        this.stepCommentFactory = new StepCommentFactory();
        this.descriptionFactory = new DescriptionFactory(new AsProviderFactory(), new AnnotatedDescriptionFactory());
        this.configuration = new DefaultConfiguration();
        initializeDependentOnConfiguration();
    }

    public MockScenarioModelBuilder(CurrentScenarioState currentScenarioState, StepCommentFactory stepCommentFactory, DescriptionFactory descriptionFactory) {
        this.currentScenarioState = currentScenarioState;
        this.stepCommentFactory = stepCommentFactory;
        this.descriptionFactory = descriptionFactory;
        this.configuration = new DefaultConfiguration();
        initializeDependentOnConfiguration();
    }

    public MockScenarioModelBuilder(CurrentScenarioState currentScenarioState, StepCommentFactory stepCommentFactory, DescriptionFactory descriptionFactory, AbstractJGivenConfiguration configuration) {
        this.currentScenarioState = currentScenarioState;
        this.stepCommentFactory = stepCommentFactory;
        this.descriptionFactory = descriptionFactory;
        this.configuration = configuration;
        initializeDependentOnConfiguration();
    }

    private void initializeDependentOnConfiguration() {
        formatterFactory = new ParameterFormatterFactory(configuration);
        stepModelFactory = new StepModelFactory(currentScenarioState, formatterFactory, descriptionFactory);
        annotationTagExtractor = AnnotationTagExtractor.forConfig(configuration);
    }

    public void setReportModel(ReportModel reportModel) {
        this.reportModel = reportModel;
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

        scenarioCaseModel = new ScenarioCaseModel();

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
        StepModel stepModel = stepModelFactory.create(paramMethod, arguments, mode, introWord);

        if (introWord != null) {
            introWord = null;
        }

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

    public void setArguments(List<String> arguments) {
        scenarioCaseModel.setExplicitArguments(arguments);
    }

    public void setParameterNames(List<String> parameterNames) {
        scenarioModel.setExplicitParameters(removeUnderlines(parameterNames));
    }

    private static List<String> removeUnderlines(List<String> parameterNames) {
        List<String> result = Lists.newArrayListWithCapacity(parameterNames.size());
        for (String paramName : parameterNames) {
            result.add(WordUtil.fromSnakeCase(paramName));
        }
        return result;
    }

    @Deprecated
    public void setSuccess(boolean success) {
        scenarioCaseModel.setSuccess(success);
    }

    public void setStatus(ExecutionStatus status) {
        scenarioCaseModel.setStatus(status);
    }

    public void setException(Throwable throwable) {
        scenarioCaseModel.setErrorMessage(throwable.getClass()
                                                   .getName() + ": " + throwable.getMessage());
        scenarioCaseModel.setStackTrace(getStackTrace(throwable));
    }

    private List<String> getStackTrace(Throwable throwable) {
        if (FILTER_STACK_TRACE) {
            return ObjectUtils.getFilteredStackTrace(throwable, STACK_TRACE_FILTER);
        } else {
            return ObjectUtils.getStackTrace(throwable);
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
                    currentStep.setStatus(getStatusFromNestedSteps(currentStep.getNestedSteps()));
                }
                parentSteps.pop();
            }
        }

        if (!hasNestedSteps && !parentSteps.isEmpty()) {
            currentStep = parentSteps.peek();
        }
    }

    private StepStatus getStatusFromNestedSteps(List<StepModel> nestedSteps) {
        StepStatus status = StepStatus.PASSED;
        for (StepModel nestedModel : nestedSteps) {
            StepStatus nestedStatus = nestedModel.getStatus();

            switch (nestedStatus) {
                case FAILED:
                    return StepStatus.FAILED;
                case PENDING:
                    status = StepStatus.PENDING;
                    break;
            }
        }
        return status;
    }

    @Override
    public void scenarioFailed(Throwable e) {
        setSuccess(false);
        setStatus(ExecutionStatus.FAILED);
        setException(e);
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
        setParameterNames(ArgumentUtils.getNames(namedArguments));

        // must come at last
        scenarioModel.setTestMethodName(method.getName());

        List<ObjectFormatter<?>> formatter = formatterFactory.create(method, namedArguments);
        setArguments(ParameterFormatterUtils.toStringList(formatter, ArgumentUtils.getValues(namedArguments)));

        setCaseDescription(testClass, method, namedArguments);
    }

    private void setCaseDescription(
            Class<?> testClass,
            Method method,
            List<NamedArgument> namedArguments
    ) {

        CaseAs annotation = null;
        if (method.isAnnotationPresent(CaseAs.class)) {
            annotation = method.getAnnotation(CaseAs.class);
        } else if (testClass.isAnnotationPresent(CaseAs.class)) {
            annotation = testClass.getAnnotation(CaseAs.class);
        }

        if (annotation != null) {
            CaseAsProvider caseDescriptionProvider = ReflectionUtil.newInstance(annotation.provider());
            String value = annotation.value();
            List<?> values;
            if (annotation.formatValues()) {
                values = scenarioCaseModel.getExplicitArguments();
            } else {
                values = ArgumentUtils.getValues(namedArguments);
            }
            String caseDescription = caseDescriptionProvider.as(value, scenarioModel.getExplicitParameters(), values);
            scenarioCaseModel.setDescription(caseDescription);
        }
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

        if (scenarioCaseModel.getCaseNr() == 1) {
            addTags(testClass.getAnnotations());
            addTags(method.getAnnotations());
        }
    }

    public void addTags(Annotation... annotations) {
        for (Annotation annotation : annotations) {
            addTags(annotationTagExtractor.extract(annotation));
        }
    }

    private void addTags(List<Tag> tags) {
        if (tags.isEmpty()) {
            return;
        }

        if (reportModel != null) {
            this.reportModel.addTags(tags);
        }

        if (scenarioModel != null) {
            this.scenarioModel.addTags(tags);
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
        if (tagConfig == null) {
            return;
        }

        List<Tag> tags = AnnotationTagUtils.toTags(tagConfig, Optional.empty());
        if (tags.isEmpty()) {
            return;
        }

        if (values.length > 0) {
            addTags(AnnotationTagUtils.getExplodedTags(Iterables.getOnlyElement(tags), values, null, tagConfig));
        } else {
            addTags(tags);
        }
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
