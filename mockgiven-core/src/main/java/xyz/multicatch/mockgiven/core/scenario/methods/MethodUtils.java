package xyz.multicatch.mockgiven.core.scenario.methods;

import java.lang.reflect.Method;
import java.util.Optional;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.AsProvider;
import com.tngtech.jgiven.annotation.Description;
import com.tngtech.jgiven.annotation.Hidden;
import xyz.multicatch.mockgiven.core.annotations.Prefixed;
import xyz.multicatch.mockgiven.core.annotations.as.AsProviderFactory;
import xyz.multicatch.mockgiven.core.stages.DescriptiveStage;

public class MethodUtils {

    public static String getDescription(Object currentStage, Method method) {
        if (method.isAnnotationPresent(Hidden.class)) {
            return "";
        }

        Description description = method.getAnnotation(Description.class);
        if (description != null) {
            return description.value();
        }

        As as = method.getAnnotation(As.class);
        AsProvider provider = AsProviderFactory.create(as);

        String prefix = extractPrefix(currentStage)
                .filter(ignored -> method.isAnnotationPresent(Prefixed.class))
                .map(p -> p + " ")
                .orElse("");

        return prefix + provider.as(as, method);
    }

    private static Optional<String> extractPrefix(Object stage) {
        if (stage instanceof DescriptiveStage) {
            DescriptiveStage descriptiveStage = (DescriptiveStage) stage;
            String prefix = descriptiveStage.getCurrentPrefix();

            if (prefix != null) {
                return Optional.of(prefix);
            } else {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
