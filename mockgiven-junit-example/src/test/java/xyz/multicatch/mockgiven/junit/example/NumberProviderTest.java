package xyz.multicatch.mockgiven.junit.example;

import java.math.BigInteger;
import org.assertj.core.api.BigIntegerAssert;
import org.junit.Test;
import xyz.multicatch.mockgiven.junit.SimpleMockScenarioTest;

public class NumberProviderTest extends SimpleMockScenarioTest {

    private NumberProvider numberProvider = new NumberProvider();

    @Test
    public void providerShouldProvideOne() {
        when("number provider provides number").by(() -> numberProvider.provide());
        then().assertUsing(BigIntegerAssert.class)
              .thatResult()
              .isEqualTo(BigInteger.ONE);
    }
}
