package xyz.multicatch.mockgiven.core.scenario;

import com.google.common.reflect.TypeToken;
import xyz.multicatch.mockgiven.core.resources.TextResource;
import xyz.multicatch.mockgiven.core.resources.TextResourceProvider;
import xyz.multicatch.mockgiven.core.scenario.model.MockScenario;
import xyz.multicatch.mockgiven.core.scenario.state.CurrentScenarioState;
import xyz.multicatch.mockgiven.core.stages.Action;
import xyz.multicatch.mockgiven.core.stages.MockedStages;
import xyz.multicatch.mockgiven.core.stages.Outcome;
import xyz.multicatch.mockgiven.core.stages.State;

@SuppressWarnings("unchecked")
public abstract class MockScenarioTestBase<STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> {

    private final CurrentScenarioState currentScenarioState = new CurrentScenarioState();

    protected MockScenario<STATE, ACTION, OUTCOME> createScenario(TextResourceProvider textResourceProvider) {
        Class<STATE> givenClass = (Class<STATE>) new TypeToken<STATE>(getClass()) {}.getRawType();
        Class<ACTION> whenClass = (Class<ACTION>) new TypeToken<ACTION>(getClass()) {}.getRawType();
        Class<OUTCOME> thenClass = (Class<OUTCOME>) new TypeToken<OUTCOME>(getClass()) {}.getRawType();

        return new MockScenario(textResourceProvider, givenClass, whenClass, thenClass);
    }

    public <T> STATE given() {
        STATE given = getScenario().given();
        return MockedStages.bindNull(given);
    }

    public <T> STATE given(T methodCall) {
        STATE given = getScenario().given();
        return MockedStages.bindCall(given, methodCall);
    }

    public <T> STATE given(
            String description,
            T methodCall
    ) {
        STATE given = getScenario().given();
        return MockedStages.bindCall(given, description, methodCall);
    }

    public ACTION when(String description) {
        return (ACTION) getScenario().when()
                                     .$(description);
    }

    public OUTCOME then() {
        return getScenario().then();
    }

    public <S extends State<?>, T> S and(
            String description,
            T methodCall
    ) {
        return getCurrentState(TextResource.AND, description, methodCall);
    }

    public <S extends State<?>, T> S but(
            String description,
            T methodCall
    ) {
        return getCurrentState(TextResource.BUT, description, methodCall);
    }

    protected <S extends State<?>, T> S getCurrentState(TextResource intro, String description, T methodCall) {
        getScenario().addIntroWord(intro);
        S currentStage = (S) currentScenarioState.getCurrentStage();
        return MockedStages.bindCall(currentStage, description, methodCall);
    }

    public <A extends Action<?>> A and(String description) {
        return getCurrentAction(TextResource.AND, description);
    }

    public <A extends Action<?>> A but(String description) {
        return getCurrentAction(TextResource.BUT, description);
    }

    protected <A extends Action<?>> A getCurrentAction(TextResource intro, String description) {
        getScenario().addIntroWord(intro);
        A currentStage = (A) currentScenarioState.getCurrentStage();
        return (A) currentStage.$(description);
    }

    public <S> S and() {
        return getCurrentStage(TextResource.AND);
    }

    public <S> S but() {
        return getCurrentStage(TextResource.BUT);
    }

    protected <S> S getCurrentStage(TextResource intro) {
        getScenario().addIntroWord(intro);
        return (S) currentScenarioState.getCurrentStage();
    }

    public abstract MockScenario<STATE, ACTION, OUTCOME> getScenario();

}
