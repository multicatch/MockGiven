package xyz.multicatch.mockgiven.core.scenario.methods.arguments;

import java.util.List;
import java.util.stream.Collectors;
import com.tngtech.jgiven.report.model.NamedArgument;

public class ArgumentUtils {

    public static List<Object> getValues(List<NamedArgument> namedArguments) {
        return namedArguments.stream()
                             .map(NamedArgument::getValue)
                             .collect(Collectors.toList());
    }

    public static List<String> getNames(List<NamedArgument> namedArguments) {
        return namedArguments.stream()
                             .map(namedArgument -> namedArgument.name)
                             .collect(Collectors.toList());
    }

}
