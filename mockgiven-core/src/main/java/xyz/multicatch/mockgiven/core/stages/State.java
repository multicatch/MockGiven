package xyz.multicatch.mockgiven.core.stages;

import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.Hidden;
import com.tngtech.jgiven.annotation.IntroWord;
import xyz.multicatch.mockgiven.core.annotations.Localized;
import xyz.multicatch.mockgiven.core.annotations.Prefixed;
import xyz.multicatch.mockgiven.core.resources.TextResource;

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
    @Localized(TextResource.GIVEN)
    public <T> SELF given(String description, T methodCall) {
        return (SELF) MockedStages.bindCall(this, description, methodCall);
    }

    @IntroWord
    @Localized(TextResource.AND)
    public <T> SELF and(String description, T methodCall) {
        return (SELF) MockedStages.bindCall(this, description, methodCall);
    }

    @IntroWord
    @Localized(TextResource.BUT)
    public <T> SELF but(String description, T methodCall) {
        return (SELF) MockedStages.bindCall(this, description, methodCall);
    }

    @IntroWord
    @Localized(TextResource.WITH)
    public <T> SELF with(String description, T methodCall) {
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

    @Prefixed
    @As("$1")
    public SELF as(String description, Runnable runnable) {
        runnable.run();
        return self();
    }
}
