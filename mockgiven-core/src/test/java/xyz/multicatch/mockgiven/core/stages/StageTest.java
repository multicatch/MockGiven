package xyz.multicatch.mockgiven.core.stages;

import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.tngtech.jgiven.annotation.IntroWord;
import xyz.multicatch.mockgiven.core.annotations.Localized;
import xyz.multicatch.mockgiven.core.resources.TextResource;

class StageTest {

    @DisplayName("Given should only add a localized intro word")
    @Test
    void givenShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(Stage.class.getMethod("given"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.GIVEN));

        Stage stage = new Stage();
        Assertions.assertThat(stage.given())
                  .isInstanceOf(Stage.class);
    }

    @DisplayName("When should only add a localized intro word")
    @Test
    void whenShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(Stage.class.getMethod("when"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.WHEN));

        Stage stage = new Stage();
        Assertions.assertThat(stage.when())
                  .isInstanceOf(Stage.class);
    }

    @DisplayName("Then should only add a localized intro word")
    @Test
    void thenShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(Stage.class.getMethod("then"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.THEN));

        Stage stage = new Stage();
        Assertions.assertThat(stage.then())
                  .isInstanceOf(Stage.class);
    }

    @DisplayName("And should only add a localized intro word")
    @Test
    void andShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(Stage.class.getMethod("and"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.AND));

        Stage stage = new Stage();
        Assertions.assertThat(stage.and())
                  .isInstanceOf(Stage.class);
    }

    @DisplayName("With should only add a localized intro word")
    @Test
    void withShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(Stage.class.getMethod("with"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.WITH));

        Stage stage = new Stage();
        Assertions.assertThat(stage.with())
                  .isInstanceOf(Stage.class);
    }

    @DisplayName("But should only add a localized intro word")
    @Test
    void butShouldAddIntroWord() throws NoSuchMethodException {
        Assertions.assertThat(Stage.class.getMethod("but"))
                  .matches(method -> method.isAnnotationPresent(IntroWord.class))
                  .matches(method -> isLocalizedAs(method, TextResource.BUT));

        Stage stage = new Stage();
        Assertions.assertThat(stage.but())
                  .isInstanceOf(Stage.class);
    }

    private boolean isLocalizedAs(
            Method method,
            TextResource textResource
    ) {
        return method.isAnnotationPresent(Localized.class) && textResource.equals(method.getAnnotation(Localized.class)
                                                                                        .value());
    }

}