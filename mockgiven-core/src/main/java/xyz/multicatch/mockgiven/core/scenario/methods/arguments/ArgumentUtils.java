package xyz.multicatch.mockgiven.core.scenario.methods.arguments;

import java.util.List;
import com.google.common.collect.Lists;
import com.tngtech.jgiven.report.model.NamedArgument;

public class ArgumentUtils {

    public static List<Object> getValues(List<NamedArgument> namedArguments) {
        List<Object> result = Lists.newArrayList();
        for (NamedArgument a : namedArguments) {
            result.add(a.value);
        }
        return result;
    }

    public static List<String> getNames(List<NamedArgument> namedArguments) {
        List<String> result = Lists.newArrayList();
        for (NamedArgument a : namedArguments) {
            result.add(a.name);
        }
        return result;
    }

}
