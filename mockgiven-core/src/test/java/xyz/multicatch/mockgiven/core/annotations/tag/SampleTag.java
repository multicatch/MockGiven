package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.tngtech.jgiven.annotation.IsTag;
import com.tngtech.jgiven.impl.tag.DefaultTagDescriptionGenerator;
import com.tngtech.jgiven.impl.tag.DefaultTagHrefGenerator;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IsTag(
        explodeArray = false,
        ignoreValue = true,
        value = "Tag value",
        description = "Sophisticated description",
        descriptionGenerator = DefaultTagDescriptionGenerator.class,
        name = "Tag name",
        prependType = true,
        cssClass = "tag-class",
        color = "blue",
        style = "nice",
        href = "http://multicatch.xyz",
        hrefGenerator = DefaultTagHrefGenerator.class,
        showInNavigation = false
)
public @interface SampleTag {
}


