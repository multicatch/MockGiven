package xyz.multicatch.mockgiven.core.scenario;

import com.google.common.reflect.TypeToken;
import xyz.multicatch.mockgiven.core.scenario.model.MockScenario;
import xyz.multicatch.mockgiven.core.stages.Action;
import xyz.multicatch.mockgiven.core.stages.MockedStages;
import xyz.multicatch.mockgiven.core.stages.Outcome;
import xyz.multicatch.mockgiven.core.stages.State;

@SuppressWarnings("unchecked")
public abstract class MockScenarioTestBase<STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> {

    protected MockScenario<STATE, ACTION, OUTCOME> createScenario() {
        Class<STATE> givenClass = (Class<STATE>) new TypeToken<STATE>(getClass()) {}.getRawType();
        Class<ACTION> whenClass = (Class<ACTION>) new TypeToken<ACTION>(getClass()) {}.getRawType();
        Class<OUTCOME> thenClass = (Class<OUTCOME>) new TypeToken<OUTCOME>(getClass()) {}.getRawType();

        return new MockScenario(givenClass, whenClass, thenClass);
    }

    public <T> STATE given() {
        STATE given = getScenario().given();
        return (STATE) MockedStages.bindNull(given);
    }

    public <T> STATE given(T methodCall) {
        STATE given = getScenario().given();
        return (STATE) MockedStages.bindCall(given, methodCall);
    }

    public <T> STATE given(
            String description,
            T methodCall
    ) {
        STATE given = getScenario().given();
        return (STATE) MockedStages.bindCall(given, description, methodCall);
    }

    public ACTION when(String description) {
        return (ACTION) getScenario().when()
                                     .$(description);
    }

    public OUTCOME then() {
        return getScenario().then();
    }

    public abstract MockScenario<STATE, ACTION, OUTCOME> getScenario();

}
