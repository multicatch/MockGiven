package xyz.multicatch.mockgiven.core.stages;

import org.assertj.core.api.Assert;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.ScenarioStage;

public abstract class Outcome<SELF extends Outcome<?>> extends Stage<SELF> {
    @ScenarioStage
    AssertionStage assertionStage;

    @Hidden
    @SuppressWarnings("unchecked")
    public <ASSERT extends Assert<?, ?>> AssertionStage<ASSERT> assertUsing(@Hidden Class<ASSERT> assertionClass) {
        return assertionStage.assertUsing(assertionClass);
    }

    @Hidden
    public <T> T asInvocationOf(T mock) {
        return Mockito.verify(mock);
    }

    @Hidden
    public <T> T asInvocationOf(T mock, VerificationMode mode) {
        return Mockito.verify(mock, mode);
    }
}
