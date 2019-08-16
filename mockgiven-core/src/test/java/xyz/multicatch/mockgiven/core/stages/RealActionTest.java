package xyz.multicatch.mockgiven.core.stages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RealActionTest {

    @DisplayName("By method should supply result to a result field")
    @Test
    void byShouldSupplyResult() {
        RealAction realAction = new RealAction();

        Assertions.assertThat(realAction)
                .extracting("_result")
                .containsOnlyNulls();

        Assertions.assertThat(realAction.by(() -> "test"))
                .isInstanceOf(RealAction.class);

        Assertions.assertThat(realAction)
                .extracting("_result")
                .containsExactly("test");
    }

    @DisplayName("As method should only run supplied runnable")
    @Test
    void asShouldRunRunnable() {
        RealAction realAction = new RealAction();
        Runnable runnable = Mockito.spy(Runnable.class);

        Assertions.assertThat(realAction)
                .extracting("_result")
                .containsOnlyNulls();

        Assertions.assertThat(realAction.as(runnable))
                .isInstanceOf(RealAction.class);

        Mockito.verify(runnable)
                .run();

        Assertions.assertThat(realAction)
                .extracting("_result")
                .containsOnlyNulls();
    }

    @DisplayName("Yield result method should return result")
    @Test
    void yieldShouldReturnValue() {
        RealAction realAction = new RealAction();

        Assertions.assertThat(realAction)
                .extracting("_result")
                .containsOnlyNulls();

        Assertions.assertThat(realAction.yieldUsing(() -> "test"))
                .isInstanceOf(String.class)
                .isEqualTo("test");

        Assertions.assertThat(realAction)
                .extracting("_result")
                .containsOnlyNulls();
    }

}