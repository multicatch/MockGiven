package xyz.multicatch.mockgiven.core.annotations.description;

import java.lang.reflect.Method;
import java.util.Optional;
import com.tngtech.jgiven.annotation.Description;

public class AnnotatedDescriptionFactory {
    public Optional<String> create(Method method) {
        Description description = method.getAnnotation(Description.class);
        if (description != null) {
            return Optional.of(description.value());
        }

        return Optional.empty();
    }
}
