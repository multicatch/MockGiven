package xyz.multicatch.mockgiven.core.scenario.methods;

import java.lang.reflect.Method;
import java.util.Optional;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.AsProvider;
import com.tngtech.jgiven.annotation.Hidden;
import xyz.multicatch.mockgiven.core.annotations.Localized;
import xyz.multicatch.mockgiven.core.annotations.Prefixed;
import xyz.multicatch.mockgiven.core.annotations.as.AsProviderFactory;
import xyz.multicatch.mockgiven.core.annotations.description.AnnotatedDescriptionFactory;
import xyz.multicatch.mockgiven.core.resources.TextResourceProvider;

public class DescriptionFactory {
    private final AsProviderFactory asProviderFactory;
    private final AnnotatedDescriptionFactory annotatedDescriptionFactory;
    private final TextResourceProvider textResourceProvider;

    public DescriptionFactory(
            AsProviderFactory asProviderFactory,
            AnnotatedDescriptionFactory annotatedDescriptionFactory,
            TextResourceProvider textResourceProvider
    ) {
        this.asProviderFactory = asProviderFactory;
        this.annotatedDescriptionFactory = annotatedDescriptionFactory;
        this.textResourceProvider = textResourceProvider;
    }

    public String create(
            Object currentStage,
            Method method
    ) {
        if (method.isAnnotationPresent(Hidden.class)) {
            return "";
        }

        if (method.isAnnotationPresent(Localized.class)) {
            Localized annotation = method.getAnnotation(Localized.class);
            return textResourceProvider.get(annotation.value());
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
