package xyz.multicatch.mockgiven.core.stages;

import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

class ActionTest {

    @DisplayName("Action should have correct methods")
    @Test
    void actionShouldHaveCorrectMethods() throws NoSuchMethodException {
        Assertions.assertThat(Action.class)
                  .hasMethods("by", "as", "yieldUsing");

        Predicate<Method> isHidden = method -> method.isAnnotationPresent(Hidden.class);
        Assertions.assertThat(Action.class.getDeclaredMethod("by", Supplier.class))
                  .matches(isHidden);

        Assertions.assertThat(Action.class.getDeclaredMethod("as", Runnable.class))
                  .matches(isHidden);

        Assertions.assertThat(Action.class.getDeclaredMethod("yieldUsing", Supplier.class))
                  .matches(isHidden);
    }

    @DisplayName("Action should have a provided result")
    @Test
    void actionShouldHaveResultObject() throws NoSuchFieldException {
        Assertions.assertThat(Action.class)
                  .hasDeclaredFields("_result");

        Assertions.assertThat(Action.class.getDeclaredField("_result"))
                  .matches(field -> Object.class.equals(field.getType()))
                  .matches(field -> field.isAnnotationPresent(ProvidedScenarioState.class));
    }

}