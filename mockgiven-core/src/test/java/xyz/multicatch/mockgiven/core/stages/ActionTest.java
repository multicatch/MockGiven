package xyz.multicatch.mockgiven.core.stages;

import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ActionTest {

    @DisplayName("Action should have correct methods")
    @Test
    void actionShouldHaveCorrectMethods() throws NoSuchMethodException {
        Assertions.assertThat(Action.class)
                .hasMethods("by", "as", "yieldUsing");

        Consumer<Method> isHidden = method -> method.isAnnotationPresent(Hidden.class);
        Assertions.assertThat(Action.class.getDeclaredMethod("by", Supplier.class))
                .satisfies(isHidden);

        Assertions.assertThat(Action.class.getDeclaredMethod("as", Runnable.class))
                .satisfies(isHidden);

        Assertions.assertThat(Action.class.getDeclaredMethod("yieldUsing", Supplier.class))
                .satisfies(isHidden);
    }

    @DisplayName("Action should have a provided result")
    @Test
    void actionShouldHaveResultObject() throws NoSuchFieldException {
        Assertions.assertThat(Action.class)
                .hasDeclaredFields("_result");

        Assertions.assertThat(Action.class.getDeclaredField("_result"))
                .satisfies(field -> Object.class.equals(field.getType()))
                .satisfies(field -> field.isAnnotationPresent(ProvidedScenarioState.class));
    }

}