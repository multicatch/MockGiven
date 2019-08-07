package xyz.multicatch.mockgiven.core.annotations.as;

import java.lang.reflect.Method;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.AsProvider;

class MockedAsProvider implements AsProvider {

    @Override
    public String as(
            As annotation,
            Method method
    ) {
        return null;
    }

    @Override
    public String as(
            As annotation,
            Class<?> scenarioClass
    ) {
        return null;
    }
}
