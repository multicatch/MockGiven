package xyz.multicatch.mockgiven.core.stages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MockedStagesTest {

    @DisplayName("Null should be bound to a state")
    @Test
    void shouldBindNull() {
        State state = Mockito.spy(State.class);

        Assertions.assertThat(MockedStages.bindNull(state))
                  .isInstanceOf(State.class);

        Mockito.verify(state)
               .bindDescription(Mockito.eq(null));

        Mockito.verify(state)
               .bindMethodCall(Mockito.eq(null));
    }

    @DisplayName("No description and a method call should be bound to a state")
    @Test
    void shouldBindCallWithoutDescription() {
        State state = Mockito.spy(State.class);
        State mock = Mockito.mock(State.class);

        Assertions.assertThat(MockedStages.bindCall(state, mock.and()))
                  .isInstanceOf(State.class);

        Mockito.verify(state)
               .bindDescription(Mockito.eq(null));

        Mockito.verify(state)
               .bindMethodCall(Mockito.eq(mock.and()));
    }

    @DisplayName("Description and a method call should be bound to a state")
    @Test
    void shouldBindCallWithDescription() {
        State state = Mockito.spy(State.class);
        State mock = Mockito.mock(State.class);

        Assertions.assertThat(MockedStages.bindCall(state, "test", mock.and()))
                  .isInstanceOf(State.class);

        Mockito.verify(state)
               .bindDescription(Mockito.eq("test"));

        Mockito.verify(state)
               .bindMethodCall(Mockito.eq(mock.and()));
    }
}