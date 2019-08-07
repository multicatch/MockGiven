package xyz.multicatch.mockgiven.core.annotations.description;

import java.lang.reflect.Method;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.Description;

class AnnotatedDescriptionFactoryTest {
    private AnnotatedDescriptionFactory annotatedDescriptionFactory = new AnnotatedDescriptionFactory();

    @DisplayName("Description should not be created from non-annotated method")
    @Test
    void shouldNotCreateDescription() {
        Method method = Mockito.mock(Method.class);
        Optional<String> description = annotatedDescriptionFactory.create(method);
        Assertions.assertThat(description)
                  .isNotPresent();
    }

    @DisplayName("Description should be created from Description-annotated method")
    @Test
    void shouldCreateDescription() {
        Description descriptionAnnotation = Mockito.mock(Description.class);
        Mockito.when(descriptionAnnotation.value())
               .thenReturn("TEST");

        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getAnnotation(Mockito.eq(Description.class)))
               .thenReturn(descriptionAnnotation);

        Optional<String> description = annotatedDescriptionFactory.create(method);
        Assertions.assertThat(description)
                  .isPresent()
                  .hasValue("TEST");
    }
}