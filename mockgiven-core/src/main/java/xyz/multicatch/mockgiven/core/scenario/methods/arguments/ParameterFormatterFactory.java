package xyz.multicatch.mockgiven.core.scenario.methods.arguments;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import com.tngtech.jgiven.config.AbstractJGivenConfiguration;
import com.tngtech.jgiven.format.ArgumentFormatter;
import com.tngtech.jgiven.format.DefaultFormatter;
import com.tngtech.jgiven.format.ObjectFormatter;
import com.tngtech.jgiven.impl.format.ParameterFormattingUtil;
import com.tngtech.jgiven.report.model.NamedArgument;
import com.tngtech.jgiven.report.model.StepFormatter;

public class ParameterFormatterFactory {
    private static final StepFormatter.Formatting<?, ?> DEFAULT_FORMATTING = new StepFormatter.ArgumentFormatting<ArgumentFormatter<Object>, Object>(
            new DefaultFormatter<>());

    private final ParameterFormattingUtil parameterFormattingUtil;

    public ParameterFormatterFactory(AbstractJGivenConfiguration configuration) {
        this.parameterFormattingUtil = new ParameterFormattingUtil(configuration);
    }

    public List<ObjectFormatter<?>> create(
            Parameter[] parameters,
            List<NamedArgument> namedArguments
    ) {
        Class<?>[] parameterTypes = Arrays.stream(parameters)
                                          .map(Parameter::getType)
                                          .toArray(Class<?>[]::new);

        Annotation[][] parameterAnnotations = Arrays.stream(parameters)
                                                    .map(Parameter::getAnnotations)
                                                    .toArray(Annotation[][]::new);
        return parameterFormattingUtil.getFormatter(parameterTypes,
                ArgumentUtils.getNames(namedArguments),
                parameterAnnotations
        );
    }

    static StepFormatter.Formatting<?, ?> createDefault() {
        return DEFAULT_FORMATTING;
    }
}
