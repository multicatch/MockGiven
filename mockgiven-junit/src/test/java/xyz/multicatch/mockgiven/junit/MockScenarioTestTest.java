package xyz.multicatch.mockgiven.junit;

import org.junit.Test;
import org.mockito.Mockito;

public class MockScenarioTestTest extends SimpleMockScenarioTest {

    @Test
    public void test() {
        MockTest mock = Mockito.mock(MockTest.class);
        MockTest mock2 = Mockito.mock(MockTest.class);
        this.given("a number two provider", mock.two()).returns("4")
            .but("the second provider", mock2.two()).returns("3");
        when("a number is requested").by(mock::two);
        then().resultIs("3");
    }
}