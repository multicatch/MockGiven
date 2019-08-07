import java.math.BigInteger;
import org.assertj.core.api.BigIntegerAssert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import xyz.multicatch.mockgiven.junit.SimpleMockScenarioTest;

public class SimpleMockScenarioExample extends SimpleMockScenarioTest {

    @Mock
    private NumberProvider numberProvider;

    @Mock
    private InterestingCounter interestingCounter;

    @InjectMocks
    private QuirkyCalculator quirkyCalculator;

    @Test
    public void shouldExponentiateNumber() {
        given("a number provider", numberProvider.provide()).returns(BigInteger.TEN);
        when("a number is exponentiated to the power of 2").by(() -> quirkyCalculator.exponentiate(2));
        then().assertUsing(BigIntegerAssert.class)
              .thatResult()
              .isEqualTo(100);
    }

    @Test
    public void shouldIncrementCounter() {
        given("an initial counter value", interestingCounter.get()).is(BigInteger.ONE);
        when("calculator increments counter").as(() -> quirkyCalculator.incrementCounter());
        BigInteger two = new BigInteger("2");
        then().$("counter value has changed").asInvocationOf(interestingCounter).set(Mockito.eq(two));
    }

    @Test
    public void shouldRetrieveNextCounterValue() {
        given("counter value", interestingCounter.get()).is(BigInteger.ZERO);
        BigInteger nextValue =
                when("calculator returns next counter value").yieldUsing(() -> quirkyCalculator.nextCounterValue());
        then().assertUsing(BigIntegerAssert.class)
              .that(nextValue)
              .isEqualTo(BigInteger.ONE);
    }

}
