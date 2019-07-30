package xyz.multicatch.mockgiven.core.stages;

import java.util.function.Supplier;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;

public abstract class Action<SELF extends Action<?>> extends Stage<SELF> {
    @ProvidedScenarioState(resolution = ScenarioState.Resolution.NAME)
    private Object _result;

    @Hidden
    public SELF by(Supplier supplier) {
        this._result = supplier.get();
        return self();
    }

    @Hidden
    public <T> T yieldUsing(Supplier<T> supplier) {
        return supplier.get();
    }
}
