package xyz.multicatch.mockgiven.core.stages;

public class MockedStages {

    @SuppressWarnings("unchecked")
    public static <STATE extends State<?>> STATE bindNull(STATE state) {
        return (STATE) state.bindDescription(null)
                            .bindMethodCall(null);
    }

    @SuppressWarnings("unchecked")
    public static <STATE extends State<?>, T> STATE bindCall(
            STATE state,
            T methodCall
    ) {
        return (STATE) state.bindDescription(null)
                            .bindMethodCall(methodCall);
    }

    @SuppressWarnings("unchecked")
    public static <STATE extends State<?>, T> STATE bindCall(
            STATE state,
            String description,
            T methodCall
    ) {
        return (STATE) state.bindDescription(description)
                            .bindMethodCall(methodCall);
    }
}
