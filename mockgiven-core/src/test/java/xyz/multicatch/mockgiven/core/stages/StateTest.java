package xyz.multicatch.mockgiven.core.stages;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.IntroWord;
import xyz.multicatch.mockgiven.core.annotations.Localized;
import xyz.multicatch.mockgiven.core.annotations.Prefixed;
import xyz.multicatch.mockgiven.core.resources.TextResource;

class StateTest {

    @DisplayName("Method call and description bindings should be hidden in reports")
    @Test
    void bindingShouldBeHidden() {
        Predicate<Method> isHidden = method -> method.isAnnotationPresent(Hidden.class);
        Assertions.assertThat(genericMethod(State.class, "bindMethodCall"))
                  .get()
                  .matches(isHidden);
        Assertions.assertThat(genericMethod(State.class, "bindDescription"))
                  .get()
                  .matches(isHidden);
    }

    private Optional<Method> genericMethod(Class type, String name) {
        return Arrays.stream(type.getDeclaredMethods()).filter(method -> name.equals(method.getName())).findFirst();
    }

    @DisplayName("Prefix (description) retrieval should be hidden in reports")
    @Test
    void prefixRetrievalShouldBeHidden() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("getCurrentPrefix"))
                  .matches(method -> method.isAnnotationPresent(Hidden.class));
    }

    @DisplayName("Given should add a localized intro word")
    @Test
    void givenShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("given"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.GIVEN));
    }

    @DisplayName("And should add a localized intro word")
    @Test
    void andShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("and"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.AND));
    }

    @DisplayName("With should add a localized intro word")
    @Test
    void withShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("with"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.WITH));
    }

    @DisplayName("But should add a localized intro word")
    @Test
    void butShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("but"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.BUT));
    }

    @DisplayName("'Is' clause should be prefixed with description")
    @Test
    void isShouldBePrefixed() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("is", Object.class))
                  .matches(method -> method.isAnnotationPresent(Prefixed.class));
    }

    @DisplayName("'Returns' clause should be prefixed with description")
    @Test
    void returnsShouldBePrefixed() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("returns", Object.class))
                  .matches(method -> method.isAnnotationPresent(Prefixed.class));
    }

    @DisplayName("Default 'as' clause should be hidden")
    @Test
    void defaultAsShouldBeHidden() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("as", Runnable.class))
                  .matches(method -> method.isAnnotationPresent(Hidden.class));
    }


    @DisplayName("Descriptive 'as' clause should be prefixed")
    @Test
    void descriptiveAsShouldBePrefixed() throws NoSuchMethodException {
        Assertions.assertThat(State.class.getMethod("as", String.class, Runnable.class))
                  .matches(method -> method.isAnnotationPresent(Prefixed.class))
                  .matches(method -> "$1".equals(method.getAnnotation(As.class).value()));
    }


    private boolean isLocalizedAs(
            Method method,
            TextResource textResource
    ) {
        return method.isAnnotationPresent(Localized.class) && textResource.equals(method.getAnnotation(Localized.class)
                                                                                        .value());
    }

}