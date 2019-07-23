package xyz.multicatch.mockgiven.core;

import java.util.function.Supplier;
import com.google.common.reflect.TypeToken;
import com.tngtech.jgiven.impl.Scenario;
import com.tngtech.jgiven.integration.CanWire;
import xyz.multicatch.mockgiven.core.stages.Action;
import xyz.multicatch.mockgiven.core.stages.MockedStages;
import xyz.multicatch.mockgiven.core.stages.Outcome;
import xyz.multicatch.mockgiven.core.stages.State;

@SuppressWarnings("unchecked")
public abstract class MockScenarioTestBase<STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> {

    @SuppressWarnings( { "serial", "unchecked" } )
    protected Scenario<STATE, ACTION, OUTCOME> createScenario() {
        Class<STATE> givenClass = (Class<STATE>) new TypeToken<STATE>( getClass() ) {}.getRawType();
        Class<ACTION> whenClass = (Class<ACTION>) new TypeToken<ACTION>( getClass() ) {}.getRawType();
        Class<OUTCOME> thenClass = (Class<OUTCOME>) new TypeToken<OUTCOME>( getClass() ) {}.getRawType();

        return new Scenario<STATE, ACTION, OUTCOME>( givenClass, whenClass, thenClass );
    }

    public <T> STATE given(String description, T methodCall) {
        STATE given = getScenario().given();
        return (STATE) MockedStages.bindCall(given, methodCall).$(description);
    }

    public ACTION when(String description) {
        return (ACTION) getScenario().when().$(description);
    }

    public OUTCOME then() {
        return getScenario().then();
    }

    public void section( String sectionTitle ) {
        getScenario().section( sectionTitle );
    }

    public void wireSteps( CanWire canWire ) {
        getScenario().wireSteps( canWire );
    }

    public <T> T addStage( Class<T> stageClass ) {
        return getScenario().addStage( stageClass );
    }

    public abstract Scenario<STATE, ACTION, OUTCOME> getScenario();

    public Scenario<STATE, ACTION, OUTCOME> createNewScenario() {
        return createScenario();
    }

}
