package xyz.multicatch.mockgiven.junit;

import org.assertj.core.api.StringAssert;
import org.junit.Test;
import org.mockito.Mock;

public class MockScenarioTestTest extends SimpleMockScenarioTest {

    @Mock
    MockTest mock;

    @Test
    public void test() {
        given("a number two provider", mock.two()).returns("4");

        when("a number is requested").by(mock::two);

        then().assertUsing(StringAssert.class).thatResult()
              .isEqualTo("1");
    }
}