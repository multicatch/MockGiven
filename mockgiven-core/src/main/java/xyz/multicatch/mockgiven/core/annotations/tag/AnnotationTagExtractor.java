package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tngtech.jgiven.annotation.IsTag;
import com.tngtech.jgiven.config.AbstractJGivenConfiguration;
import com.tngtech.jgiven.config.TagConfiguration;
import com.tngtech.jgiven.report.model.Tag;

public class AnnotationTagExtractor {

    private final AbstractJGivenConfiguration configuration;

    public static AnnotationTagExtractor forConfig(AbstractJGivenConfiguration configuration) {
        return new AnnotationTagExtractor(configuration);
    }

    AnnotationTagExtractor(AbstractJGivenConfiguration configuration) {
        this.configuration = configuration;
    }

    public List<Tag> extract(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        TagConfiguration tagConfig = toTagConfiguration(annotationType);
        if (tagConfig == null) {
            return Collections.emptyList();
        }

        return AnnotationTagUtils.toTags(tagConfig, Optional.of(annotation));
    }

    public List<Tag> extract(Class<? extends Annotation> annotationType) {
        List<Tag> allTags = Lists.newArrayList();

        for (Annotation a : annotationType.getAnnotations()) {
            if (a.annotationType().isAnnotationPresent(IsTag.class)) {
                List<Tag> tags = toTags(a);
                allTags.addAll(tags);
            }
        }

        return allTags;
    }

    private List<Tag> toTags(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        TagConfiguration tagConfig = toTagConfiguration(annotationType);
        if (tagConfig == null) {
            return Collections.emptyList();
        }

        return AnnotationTagUtils.toTags(tagConfig, Optional.of(annotation));
    }

    public TagConfiguration toTagConfiguration(Class<? extends Annotation> annotationType) {
        IsTag isTag = annotationType.getAnnotation(IsTag.class);
        if (isTag != null) {
            return fromIsTag(isTag, annotationType);
        }

        return configuration.getTagConfiguration(annotationType);
    }

    private TagConfiguration fromIsTag(
            IsTag isTag,
            Class<? extends Annotation> annotationType
    ) {
        String name = Strings.isNullOrEmpty(isTag.name()) ? isTag.type() : isTag.name();

        return TagConfiguration.builder(annotationType)
                               .defaultValue(isTag.value())
                               .description(isTag.description())
                               .explodeArray(isTag.explodeArray())
                               .ignoreValue(isTag.ignoreValue())
                               .prependType(isTag.prependType())
                               .name(name)
                               .descriptionGenerator(isTag.descriptionGenerator())
                               .cssClass(isTag.cssClass())
                               .color(isTag.color())
                               .style(isTag.style())
                               .tags(getTagNames(annotationType))
                               .href(isTag.href())
                               .hrefGenerator(isTag.hrefGenerator())
                               .showInNavigation(isTag.showInNavigation())
                               .build();
    }

    private List<String> getTagNames(Class<? extends Annotation> annotationType) {
        List<Tag> tags = extract(annotationType);
        List<String> tagNames = Lists.newArrayList();
        for (Tag tag : tags) {
            tagNames.add(tag.toIdString());
        }
        return tagNames;
    }
}
