package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;
import com.tngtech.jgiven.annotation.TagDescriptionGenerator;
import com.tngtech.jgiven.annotation.TagHrefGenerator;
import com.tngtech.jgiven.config.TagConfiguration;

public class UnexpectedGenerator implements TagDescriptionGenerator, TagHrefGenerator {

    private UnexpectedGenerator(String dummy) {
        System.out.println(dummy);
    }

    @Override
    public String generateDescription(
            TagConfiguration tagConfiguration,
            Annotation annotation,
            Object value
    ) {
        return null;
    }

    @Override
    public String generateHref(
            TagConfiguration tagConfiguration,
            Annotation annotation,
            Object value
    ) {
        return null;
    }
}
