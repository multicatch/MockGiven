package xyz.multicatch.mockgiven.core.stages;

import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.IntroWord;
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

    @IntroWord
    public <T> SELF given(String description, T methodCall) {
        return (SELF) MockedStages.bindCall(this, description, methodCall);
    }

    @IntroWord
    public <T> SELF and(String description, T methodCall) {
        return (SELF) MockedStages.bindCall(this, description, methodCall);
    }

    @IntroWord
    public <T> SELF or(String description, T methodCall) {
        return (SELF) MockedStages.bindCall(this, description, methodCall);
    }

    @IntroWord
    public <T> SELF but(String description, T methodCall) {
        return (SELF) MockedStages.bindCall(this, description, methodCall);
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

    @Hidden
    public SELF as(Runnable runnable) {
        runnable.run();
        return self();
    }
}
