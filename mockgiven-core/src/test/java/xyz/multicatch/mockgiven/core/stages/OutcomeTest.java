package xyz.multicatch.mockgiven.core.stages;

import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.ScenarioStage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.function.Consumer;


class OutcomeTest {
    @DisplayName("Outcome should have correct methods")
    @Test
    void outcomeShouldHaveCorrectMethods() throws NoSuchMethodException {
        Assertions.assertThat(Outcome.class)
                .hasMethods("assertUsing", "asInvocationOf");

        Consumer<Method> isHidden = method -> method.isAnnotationPresent(Hidden.class);
        Assertions.assertThat(Outcome.class.getDeclaredMethod("assertUsing", Class.class))
                .satisfies(isHidden);

        Assertions.assertThat(Outcome.class.getDeclaredMethod("asInvocationOf", Object.class))
                .satisfies(isHidden);

        Assertions.assertThat(Outcome.class.getDeclaredMethod("asInvocationOf", Object.class, VerificationMode.class))
                .satisfies(isHidden);
    }

    @DisplayName("Outcome should have a nested assertion stage")
    @Test
    void outcomeShouldHaveAssertionStage() throws NoSuchFieldException {
        Assertions.assertThat(Outcome.class)
                .hasDeclaredFields("assertionStage");

        Assertions.assertThat(Outcome.class.getDeclaredField("assertionStage"))
                .satisfies(field -> AssertionStage.class.equals(field.getType()))
                .satisfies(field -> field.isAnnotationPresent(ScenarioStage.class));
    }
}