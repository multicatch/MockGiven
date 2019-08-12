package xyz.multicatch.mockgiven.core.annotations.tag;

import com.tngtech.jgiven.annotation.IsTag;

@IsTag(
        name = "Sophisticated tag",
        ignoreValue = false,
        value = "Value test"
)
@SampleTag
public @interface SophisticatedTag {
    String[] value() default { "a", "b", "c" };
}
