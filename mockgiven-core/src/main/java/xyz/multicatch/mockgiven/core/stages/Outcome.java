package xyz.multicatch.mockgiven.core.stages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

public abstract class Outcome<SELF extends Outcome<?>> extends Stage<SELF> {
    @ExpectedScenarioState
    private Object result;

    public SELF resultIs(String s) {
        return self();
    }
}
