package xyz.multicatch.mockgiven.core.stages;

import com.tngtech.jgiven.annotation.IntroWord;
import com.tngtech.jgiven.base.StageBase;
import xyz.multicatch.mockgiven.core.annotations.Localized;
import xyz.multicatch.mockgiven.core.resources.TextResource;

public class Stage<SELF extends Stage<?>> extends StageBase<SELF> {

    @IntroWord
    @Localized(TextResource.GIVEN)
    public SELF given() {
        return self();
    }

    @IntroWord
    @Localized(TextResource.WHEN)
    public SELF when() {
        return self();
    }

    @IntroWord
    @Localized(TextResource.THEN)
    public SELF then() {
        return self();
    }

    @IntroWord
    @Localized(TextResource.AND)
    public SELF and() {
        return self();
    }

    @IntroWord
    @Localized(TextResource.WITH)
    public SELF with() {
        return self();
    }

    @IntroWord
    @Localized(TextResource.BUT)
    public SELF but() {
        return self();
    }

}
