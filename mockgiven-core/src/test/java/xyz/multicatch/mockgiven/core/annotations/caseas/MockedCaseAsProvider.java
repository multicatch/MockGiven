package xyz.multicatch.mockgiven.core.annotations.caseas;

import java.util.List;
import java.util.stream.Collectors;
import com.tngtech.jgiven.annotation.CaseAsProvider;

public class MockedCaseAsProvider implements CaseAsProvider {
    @Override
    public String as(
            String value,
            List<String> parameterNames,
            List<?> parameterValues
    ) {
        return value + ";" + String.join(",", parameterNames) + ";" + parameterValues.stream().map(Object::toString).collect(Collectors.joining(","));
    }
}
