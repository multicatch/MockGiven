package xyz.multicatch.mockgiven.core.stages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MockedStateTest {

    @DisplayName("Method call binding should be done correctly")
    @Test
    void shouldBindMethodCall() {
        MockedState state = new MockedState();
        State mock = Mockito.mock(State.class);

        Assertions.assertThat(state)
                  .extracting("methodCall")
                  .containsOnlyNulls();

        Assertions.assertThat(state.bindMethodCall(mock.and()))
                  .isEqualTo(state);

        Assertions.assertThat(state)
                  .extracting("methodCall")
                  .contains(mock.and());
    }

    @DisplayName("Description binding should be done correctly")
    @Test
    void shouldBindDescription() {
        MockedState state = new MockedState();

        Assertions.assertThat(state)
                  .extracting("description")
                  .containsOnlyNulls();

        Assertions.assertThat(state.bindDescription("test"))
                  .isEqualTo(state);

        Assertions.assertThat(state)
                  .extracting("description")
                  .contains("test");
    }

    @DisplayName("Correct prefix should be returned")
    @Test
    void shouldReturnPrefix() {
        MockedState state = new MockedState().bindDescription("test");

        Assertions.assertThat(state.getCurrentPrefix())
                  .isEqualTo("test");
    }

    @DisplayName("Intro words should be added")
    @Test
    void introWordsShouldBeAdded() {
        MockedState state = new MockedState();
        State mock = Mockito.mock(State.class);

        Assertions.assertThat(state.given("test", mock.and()))
                  .isEqualTo(state);

        Assertions.assertThat(state.and("test", mock.and()))
                  .isEqualTo(state);

        Assertions.assertThat(state.but("test", mock.and()))
                  .isEqualTo(state);

        Assertions.assertThat(state.with("test", mock.and()))
                  .isEqualTo(state);
    }

    @DisplayName("'Is' clause should stub method")
    @Test
    void isShouldStubMethod() {
        State mock = Mockito.mock(State.class);
        MockedState state = new MockedState().bindMethodCall(mock.getCurrentPrefix());

        Assertions.assertThat(state.is("test"))
                  .isEqualTo(state);
        Assertions.assertThat(mock.getCurrentPrefix())
                  .isEqualTo("test");
    }

    @DisplayName("'Returns' clause should stub method")
    @Test
    void returnsShouldStubMethod() {
        State mock = Mockito.mock(State.class);
        MockedState state = new MockedState().bindMethodCall(mock.getCurrentPrefix());

        Assertions.assertThat(state.returns("test"))
                  .isEqualTo(state);
        Assertions.assertThat(mock.getCurrentPrefix())
                  .isEqualTo("test");
    }

    @DisplayName("'As' clause should run given Runnable")
    @Test
    void asShouldRunGivenRunnable() {
        MockedState state = new MockedState();
        Runnable runnable = Mockito.spy(Runnable.class);

        Assertions.assertThat(state.as(runnable))
                  .isEqualTo(state);

        Assertions.assertThat(state.as("test", runnable))
                  .isEqualTo(state);

        Mockito.verify(runnable, Mockito.times(2))
               .run();
    }
}