package xyz.multicatch.mockgiven.core.stages;

import org.mockito.Mockito;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.Hidden;

@SuppressWarnings("unchecked")
public abstract class State<SELF extends State<?>> extends Stage<SELF> {

    private Object methodCall;

    @Hidden
    <T> SELF bindMethodCall(T methodCall) {
        this.methodCall = methodCall;
        return self();
    }

    public <T> SELF is(T object) {
        Mockito.when(methodCall)
               .thenReturn(object);

        return self();
    }

    public <T> SELF returns(T object) {
        Mockito.when(methodCall)
               .thenReturn(object);

        return self();
    }
}
