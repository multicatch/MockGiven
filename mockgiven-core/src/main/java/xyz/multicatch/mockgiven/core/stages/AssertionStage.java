package xyz.multicatch.mockgiven.core.stages;

import org.assertj.core.api.Assert;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.ScenarioState;
import xyz.multicatch.mockgiven.core.scenario.MockScenarioExecutor;

public class AssertionStage<ASSERT extends Assert<?, ?>> {
    @ExpectedScenarioState(resolution = ScenarioState.Resolution.NAME)
    private Object _result;

    private MockScenarioExecutor executor;

    private Class<ASSERT> assertionClass;

    @Hidden
    public void setExecutor(MockScenarioExecutor executor) {
        this.executor = executor;
    }

    @Hidden
    public AssertionStage<ASSERT> assertUsing(Class<ASSERT> assertionClass) {
        this.assertionClass = assertionClass;
        return this;
    }

    @SuppressWarnings("unchecked")
    @As("result")
    public ASSERT thatResult() {
        return executor.assertInterception(assertionClass, _result);
    }

    @SuppressWarnings("unchecked")
    @As("$1")
    public <ACTUAL> ASSERT that(ACTUAL actual) {
        return executor.assertInterception(assertionClass, actual);
    }

    @SuppressWarnings("unchecked")
    @As("$1 ($2)")
    public <ACTUAL> ASSERT that(String description, ACTUAL actual) {
        return executor.assertInterception(assertionClass, actual);
    }
}
