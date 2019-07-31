package xyz.multicatch.mockgiven.core.scenario.steps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import com.google.common.collect.Lists;
import com.tngtech.jgiven.annotation.ExtendedDescription;
import com.tngtech.jgiven.format.ObjectFormatter;
import com.tngtech.jgiven.impl.util.AnnotationUtil;
import com.tngtech.jgiven.report.model.InvocationMode;
import com.tngtech.jgiven.report.model.NamedArgument;
import com.tngtech.jgiven.report.model.StepFormatter;
import com.tngtech.jgiven.report.model.Word;
import xyz.multicatch.mockgiven.core.scenario.methods.DescriptionFactory;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ParameterFormatterFactory;
import xyz.multicatch.mockgiven.core.scenario.state.CurrentScenarioState;

public class StepModelFactory {
    private final CurrentScenarioState currentScenarioState;
    private final ParameterFormatterFactory parameterFormatterFactory;
    private final DescriptionFactory descriptionFactory;

    public StepModelFactory(
            CurrentScenarioState currentScenarioState,
            ParameterFormatterFactory parameterFormatterFactory,
            DescriptionFactory descriptionFactory
    ) {
        this.currentScenarioState = currentScenarioState;
        this.parameterFormatterFactory = parameterFormatterFactory;
        this.descriptionFactory = descriptionFactory;
    }

    public ExtendedStepModel create(
            Method paramMethod,
            List<NamedArgument> arguments,
            InvocationMode mode,
            Word introWord
    ) {
        ExtendedStepModel stepModel = new ExtendedStepModel();

        createModelDescription(stepModel, paramMethod);
        createModelName(stepModel, paramMethod);
        createModelWords(stepModel, introWord, paramMethod.getParameters(), arguments);

        stepModel.setStatus(mode.toStepStatus());
        return stepModel;
    }

    private void createModelDescription(
            ExtendedStepModel stepModel,
            Method paramMethod
    ) {
        ExtendedDescription extendedDescriptionAnnotation = paramMethod.getAnnotation(ExtendedDescription.class);
        if (extendedDescriptionAnnotation != null) {
            stepModel.setExtendedDescription(extendedDescriptionAnnotation.value());
        }
    }

    private void createModelName(
            ExtendedStepModel stepModel,
            Method paramMethod
    ) {
        Object currentStage = currentScenarioState.getCurrentStage();
        String name = descriptionFactory.create(currentStage, paramMethod);
        stepModel.setName(name);
    }

    private void createModelWords(
            ExtendedStepModel stepModel,
            Word introWord,
            Parameter[] parameters,
            List<NamedArgument> arguments
    ) {
        List<NamedArgument> nonHiddenArguments = filterHiddenArguments(arguments, parameters);
        List<ObjectFormatter<?>> formatter = parameterFormatterFactory.create(parameters, arguments);
        stepModel.setWords(new StepFormatter(stepModel.getName(), nonHiddenArguments, formatter).buildFormattedWords());

        if (introWord != null) {
            stepModel.addIntroWord(introWord);
        }
    }

    private List<NamedArgument> filterHiddenArguments(
            List<NamedArgument> arguments,
            Parameter[] parameters
    ) {
        Annotation[][] parameterAnnotations = Arrays.stream(parameters)
                                                    .map(Parameter::getAnnotations)
                                                    .toArray(Annotation[][]::new);
        List<NamedArgument> result = Lists.newArrayList();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            if (!AnnotationUtil.isHidden(parameterAnnotations[i])) {
                result.add(arguments.get(i));
            }
        }
        return result;
    }

}
