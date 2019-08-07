package xyz.multicatch.mockgiven.core.annotations.caseas;

import java.util.List;
import com.tngtech.jgiven.annotation.CaseAsProvider;

public class MockedCaseAsProvider implements CaseAsProvider {
    @Override
    public String as(
            String value,
            List<String> parameterNames,
            List<?> parameterValues
    ) {
        return null;
    }
}
