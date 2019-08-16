package xyz.multicatch.mockgiven.core.stages;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.MockAwareVerificationMode;

import java.lang.reflect.Field;

class ExpectedOutcomeTest {

    @DisplayName("Assert using method should return should create assertion stage")
    @Test
    void assertUsingShouldCreateAssertionStage() throws NoSuchFieldException, IllegalAccessException {
        ExpectedOutcome expectedOutcome = new ExpectedOutcome();
        AssertionStage assertionStage = Mockito.spy(AssertionStage.class);

        Field assertionStageField = Outcome.class.getDeclaredField("assertionStage");
        assertionStageField.set(expectedOutcome, assertionStage);

        Assertions.assertThat(expectedOutcome.assertUsing(ObjectAssert.class))
                .isInstanceOf(AssertionStage.class);

        Mockito.verify(assertionStage)
                .assertUsing(Mockito.eq(ObjectAssert.class));
    }

    @DisplayName("As invocation of should start Mockito verification")
    @Test
    void asInvocationOfShouldStartVerification() {
        ExpectedOutcome expectedOutcome = new ExpectedOutcome();
        Object spy = Mockito.spy(Object.class);

        expectedOutcome.asInvocationOf(spy);
        Assertions.assertThat(ThreadSafeMockingProgress.mockingProgress().pullVerificationMode())
                .isNotNull()
                .isInstanceOf(MockAwareVerificationMode.class);

        expectedOutcome.asInvocationOf(spy, Mockito.after(100L));
        Assertions.assertThat(ThreadSafeMockingProgress.mockingProgress().pullVerificationMode())
                .isNotNull()
                .isInstanceOf(MockAwareVerificationMode.class);
    }

}