package xyz.multicatch.mockgiven.junit;

import org.junit.Test;
import org.mockito.Mockito;

public class MockScenarioTestTest extends MockitoScenarioTest {

    @Test
    public void test() {
        MockTest mock = Mockito.mock(MockTest.class);
        given("a number two provider", mock.two()).returns("4");
        when("a number is requested").by(mock::two);
        then().resultIs("3");
    }
}