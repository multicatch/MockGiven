package xyz.multicatch.mockgiven.junit.example;

import java.math.BigInteger;
import org.assertj.core.api.BigIntegerAssert;
import org.junit.Test;
import xyz.multicatch.mockgiven.junit.SimpleMockScenarioTest;

public class InterestingCounterTest extends SimpleMockScenarioTest {
    private InterestingCounter interestingCounter = new InterestingCounter();

    @Test
    public void interestingCounterValueShouldBeSet() {
        given().$("initial value is 10").as(() -> interestingCounter.set(BigInteger.TEN));
        when("value is set to 1").as(() -> interestingCounter.set(BigInteger.ONE));
        then().assertUsing(BigIntegerAssert.class)
              .that("counter's value", interestingCounter.get())
              .isEqualTo(BigInteger.ONE);
    }
}
