package xyz.multicatch.mockgiven.core.stages;

import java.util.function.Supplier;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public abstract class Action<SELF extends Action<?>> extends Stage<SELF> {
    @ProvidedScenarioState
    private Object result;

    @Hidden
    public SELF by(Supplier supplier) {
        this.result = supplier.get();
        return self();
    }
}
