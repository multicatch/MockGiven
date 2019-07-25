package xyz.multicatch.mockgiven.core.scenario.methods.arguments;

import java.lang.reflect.Method;
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
            new DefaultFormatter<>() );

    private final ParameterFormattingUtil parameterFormattingUtil;

    public ParameterFormatterFactory(AbstractJGivenConfiguration configuration) {
        this.parameterFormattingUtil = new ParameterFormattingUtil(configuration);
    }

    public List<ObjectFormatter<?>> create(
            Method method,
            List<NamedArgument> namedArguments
    ) {
        return parameterFormattingUtil.getFormatter(method.getParameterTypes(), ArgumentUtils.getNames(namedArguments),
                method.getParameterAnnotations());
    }

    public static StepFormatter.Formatting<?, ?> createDefault() {
        return DEFAULT_FORMATTING;
    }
}
