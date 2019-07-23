package xyz.multicatch.mockgiven.core.stages;

public class MockedStages {

    @SuppressWarnings("unchecked")
    public static <STATE extends State<?>, T> STATE bindCall(STATE state, T methodCall) {
        return (STATE) state.bindMethodCall(methodCall);
    }
}
