package xyz.multicatch.mockgiven.core.scenario.steps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import com.google.common.collect.Lists;
import com.tngtech.jgiven.annotation.ExtendedDescription;
import com.tngtech.jgiven.format.ObjectFormatter;
import com.tngtech.jgiven.impl.util.AnnotationUtil;
import com.tngtech.jgiven.report.model.*;
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

    public StepModel create(
            Method paramMethod,
            List<NamedArgument> arguments,
            InvocationMode mode,
            Word introWord
    ) {
        StepModel stepModel = new StepModel();

        Object currentStage = currentScenarioState.getCurrentStage();
        stepModel.setName(descriptionFactory.create(currentStage, paramMethod));

        ExtendedDescription extendedDescriptionAnnotation = paramMethod.getAnnotation(ExtendedDescription.class);
        if (extendedDescriptionAnnotation != null) {
            stepModel.setExtendedDescription(extendedDescriptionAnnotation.value());
        }

        List<NamedArgument> nonHiddenArguments = filterHiddenArguments(arguments, paramMethod.getParameterAnnotations());

        List<ObjectFormatter<?>> formatter = parameterFormatterFactory.create(paramMethod, arguments);
        stepModel.setWords(new StepFormatter(stepModel.getName(), nonHiddenArguments, formatter).buildFormattedWords());

        if (introWord != null) {
            stepModel.addIntroWord(introWord);
        }

        stepModel.setStatus(mode.toStepStatus());
        return stepModel;
    }

    private List<NamedArgument> filterHiddenArguments(
            List<NamedArgument> arguments,
            Annotation[][] parameterAnnotations
    ) {
        List<NamedArgument> result = Lists.newArrayList();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            if (!AnnotationUtil.isHidden(parameterAnnotations[i])) {
                result.add(arguments.get(i));
            }
        }
        return result;
    }

}
