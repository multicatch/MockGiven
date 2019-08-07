package xyz.multicatch.mockgiven.junit.example;

import java.math.BigInteger;
import javax.inject.Inject;

public class QuirkyCalculator {

    private final NumberProvider numberProvider;
    private final InterestingCounter interestingCounter;

    @Inject
    public QuirkyCalculator(
            NumberProvider numberProvider,
            InterestingCounter interestingCounter
    ) {
        this.numberProvider = numberProvider;
        this.interestingCounter = interestingCounter;
    }

    public BigInteger exponentiate(int power) {
        return numberProvider.provide()
                             .pow(power);
    }


    public void incrementCounter() {
        interestingCounter.set(interestingCounter.get().add(BigInteger.ONE));
    }

    public BigInteger nextCounterValue() {
        return interestingCounter.get().add(BigInteger.ONE);
    }
}
