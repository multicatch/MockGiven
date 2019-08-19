package xyz.multicatch.mockgiven.core.scenario.state;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xyz.multicatch.mockgiven.core.stages.State;

class CurrentScenarioStateTest {

    private CurrentScenarioState currentScenarioState = new CurrentScenarioState();

    @DisplayName("Current stage should be set and retrieved")
    @Test
    void shouldSetAndRetrieveCurrentState() {
        State state = Mockito.mock(State.class);
        currentScenarioState.setCurrentStage(state);

        Assertions.assertThat(currentScenarioState.getCurrentStage())
                  .isEqualTo(state);
    }
}