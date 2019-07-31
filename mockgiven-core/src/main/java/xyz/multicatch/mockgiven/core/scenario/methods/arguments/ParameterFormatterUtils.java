package xyz.multicatch.mockgiven.core.scenario.methods.arguments;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.tngtech.jgiven.format.ObjectFormatter;

public class ParameterFormatterUtils {
    private ParameterFormatterUtils() {}

    public static List<String> toStringList(
            List<ObjectFormatter<?>> formatters,
            List<?> arguments
    ) {
        return arguments.stream()
                        .map(it -> ParameterFormatterUtils.format(formatters, it))
                        .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <T> String format(List<ObjectFormatter<?>> formatters, T argument) {
        ObjectFormatter<?> objectFormatter = formatters.stream()
                                                       .filter(Objects::nonNull)
                                                       .reduce((identity, accumulator) -> identity)
                                                       .orElseGet(ParameterFormatterFactory::createDefault);

        return ((ObjectFormatter<T>) objectFormatter).format(argument);
    }
}
