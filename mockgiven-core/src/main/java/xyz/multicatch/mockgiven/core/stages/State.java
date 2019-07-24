package xyz.multicatch.mockgiven.core.stages;

import org.mockito.Mockito;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.Hidden;
import xyz.multicatch.mockgiven.core.annotations.Prefixed;

@SuppressWarnings("unchecked")
public abstract class State<SELF extends State<?>> extends Stage<SELF> implements DescriptiveStage {

    private Object methodCall;
    private String description;

    @Hidden
    <T> SELF bindMethodCall(T methodCall) {
        this.methodCall = methodCall;
        return self();
    }

    @Hidden
    SELF bindDescription(String description) {
        this.description = description;
        return self();
    }

    @Hidden
    public String getCurrentPrefix() {
        return description;
    }

    @Prefixed
    public <T> SELF is(T object) {
        Mockito.when(methodCall)
               .thenReturn(object);

        return self();
    }

    @Prefixed
    public <T> SELF returns(T object) {
        Mockito.when(methodCall)
               .thenReturn(object);

        return self();
    }
}
