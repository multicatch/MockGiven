package xyz.multicatch.mockgiven.core.scenario.methods;

import java.lang.reflect.Method;
import java.util.Optional;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.AsProvider;
import com.tngtech.jgiven.annotation.Hidden;
import xyz.multicatch.mockgiven.core.annotations.Prefixed;
import xyz.multicatch.mockgiven.core.annotations.as.AsProviderFactory;
import xyz.multicatch.mockgiven.core.annotations.description.AnnotatedDescriptionFactory;

public class DescriptionFactory {
    private final AsProviderFactory asProviderFactory;
    private final AnnotatedDescriptionFactory annotatedDescriptionFactory;

    public DescriptionFactory(
            AsProviderFactory asProviderFactory,
            AnnotatedDescriptionFactory annotatedDescriptionFactory
    ) {
        this.asProviderFactory = asProviderFactory;
        this.annotatedDescriptionFactory = annotatedDescriptionFactory;
    }


    public String create(Object currentStage, Method method) {
        if (method.isAnnotationPresent(Hidden.class)) {
            return "";
        }

        Optional<String> description = annotatedDescriptionFactory.create(method);
        if (description.isPresent()) {
            return description.get();
        }

        As as = method.getAnnotation(As.class);
        AsProvider provider = asProviderFactory.create(as);

        String prefix = MethodUtils.extractPrefix(currentStage)
                .filter(ignored -> method.isAnnotationPresent(Prefixed.class))
                .map(p -> p + " ")
                .orElse("");

        return prefix + provider.as(as, method);
    }
}
