package xyz.multicatch.mockgiven.junit;

import org.assertj.core.api.StringAssert;
import org.junit.Test;
import org.mockito.Mockito;

public class MockScenarioTestTest extends SimpleMockScenarioTest {

    @Test
    public void test() {
        MockTest mock = Mockito.mock(MockTest.class);
        MockTest mock2 = Mockito.mock(MockTest.class);

        given("a number two provider", mock.two()).returns("4");
        and("the second provider", mock2.two()).returns("3");

        when("a number is requested").by(mock::two);
        String test = when("result is saved").yieldUsing(() -> "1");
        then().assertUsing(StringAssert.class).that("result", test)
              .isEqualTo("1");
        this.<StringAssert>and().isNotNull();
        then().and().assertUsing(StringAssert.class).thatResult().endsWith("4");
    }
}