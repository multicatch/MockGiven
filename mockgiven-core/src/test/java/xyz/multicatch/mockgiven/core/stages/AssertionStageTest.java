package xyz.multicatch.mockgiven.core.stages;

import java.lang.reflect.Field;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.StringAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.Hidden;
import xyz.multicatch.mockgiven.core.annotations.description.InlineWithNext;
import xyz.multicatch.mockgiven.core.scenario.MockScenarioExecutor;

class AssertionStageTest {

    @DisplayName("Setting executor in assertion stage should be hidden in report")
    @Test
    void executorSettingShouldBeHidden() throws NoSuchMethodException {
        Assertions.assertThat(AssertionStage.class.getMethod("setExecutor", MockScenarioExecutor.class))
                  .matches(method -> method.isAnnotationPresent(Hidden.class), "executor setting should be hidden");
    }

    @DisplayName("Executor should be set correctly")
    @Test
    void shouldSetExecutor() {
        AssertionStage assertionStage = new AssertionStage();
        MockScenarioExecutor executor = Mockito.mock(MockScenarioExecutor.class);

        assertionStage.setExecutor(executor);

        Assertions.assertThat(assertionStage)
                  .extracting("executor")
                  .containsExactly(executor);
    }

    @DisplayName("\"Assert using <class>\" should be hidden in report")
    @Test
    void assertionStageSettingShouldBeHidden() throws NoSuchMethodException {
        Assertions.assertThat(AssertionStage.class.getMethod("assertUsing", Class.class))
                  .matches(method -> method.isAnnotationPresent(Hidden.class));
    }

    @DisplayName("Assertion class should be set for assertions")
    @Test
    void shouldSetAssertionStage() {
        AssertionStage assertionStage = new AssertionStage();

        Assertions.assertThat(assertionStage.assertUsing(ObjectAssert.class))
                  .isInstanceOf(AssertionStage.class);

        Assertions.assertThat(assertionStage)
                  .extracting("assertionClass")
                  .containsExactly(ObjectAssert.class);
    }

    @DisplayName("Assertion on result should be properly named and formatted in report")
    @Test
    void resultAssertionShouldBeInlinedAndProperlyNamed() throws NoSuchMethodException {
        Assertions.assertThat(AssertionStage.class.getMethod("thatResult"))
                  .matches(method -> method.isAnnotationPresent(InlineWithNext.class), "assertion name is inlined")
                  .matches(method -> "result".equals(method.getAnnotation(As.class)
                                                           .value()), "assertion has proper name");
    }

    @DisplayName("Given assertion class should be used for assertions on result")
    @Test
    void shouldAssertOnResult() throws NoSuchFieldException, IllegalAccessException {
        AssertionStage assertionStage = new AssertionStage();
        MockScenarioExecutor executor = Mockito.spy(MockScenarioExecutor.class);

        assertionStage.assertUsing(StringAssert.class)
                      .setExecutor(executor);

        Field result = AssertionStage.class.getDeclaredField("_result");
        result.setAccessible(true);
        result.set(assertionStage, "test");

        Assertions.assertThat(assertionStage.thatResult())
                  .isInstanceOf(StringAssert.class);

        Mockito.verify(executor)
               .assertInterception(Mockito.eq(StringAssert.class), Mockito.eq("test"));
    }

    @DisplayName("Assertion on other objects should be properly named and formatted in report")
    @Test
    void assertionShouldBeInlinedAndProperlyNamed() throws NoSuchMethodException {
        Assertions.assertThat(AssertionStage.class.getMethod("that", Object.class))
                  .matches(method -> method.isAnnotationPresent(InlineWithNext.class), "assertion name is inlined")
                  .matches(method -> "$1".equals(method.getAnnotation(As.class)
                                                       .value()), "assertion has proper name");

        Assertions.assertThat(AssertionStage.class.getMethod("that", String.class, Object.class))
                  .matches(method -> method.isAnnotationPresent(InlineWithNext.class), "assertion name is inlined")
                  .matches(method -> "$1 ($2)".equals(method.getAnnotation(As.class)
                                                            .value()), "assertion has proper name");
    }

    @DisplayName("Given assertion class should be used for assertions on other objects")
    @Test
    void shouldAssertUsingProperClass() {
        AssertionStage assertionStage = new AssertionStage();
        MockScenarioExecutor executor = Mockito.spy(MockScenarioExecutor.class);

        assertionStage.assertUsing(StringAssert.class)
                      .setExecutor(executor);

        Assertions.assertThat(assertionStage.that("test"))
                  .isInstanceOf(StringAssert.class);

        Mockito.verify(executor)
               .assertInterception(Mockito.eq(StringAssert.class), Mockito.eq("test"));

        Assertions.assertThat(assertionStage.that("description", "test2"))
                  .isInstanceOf(StringAssert.class);

        Mockito.verify(executor)
               .assertInterception(Mockito.eq(StringAssert.class), Mockito.eq("test2"));
    }
}