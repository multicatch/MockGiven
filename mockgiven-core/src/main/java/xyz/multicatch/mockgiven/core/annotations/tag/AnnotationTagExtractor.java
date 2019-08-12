package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.base.Strings;
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

        return AnnotationTagUtils.toTags(tagConfig, annotation);
    }

    public TagConfiguration toTagConfiguration(Class<? extends Annotation> annotationType) {
        IsTag isTag = annotationType.getAnnotation(IsTag.class);
        if (isTag != null) {
            return fromIsTag(isTag, annotationType.getAnnotations(), annotationType);
        }

        return configuration.getTagConfiguration(annotationType);
    }

    private TagConfiguration fromIsTag(
            IsTag isTag,
            Annotation[] typeAnnotations,
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
                               .tags(getTagNames(typeAnnotations))
                               .href(isTag.href())
                               .hrefGenerator(isTag.hrefGenerator())
                               .showInNavigation(isTag.showInNavigation())
                               .build();
    }

    private List<String> getTagNames(Annotation[] annotations) {
        return filterTags(annotations).stream()
                                      .map(Tag::toIdString)
                                      .collect(Collectors.toList());
    }

    private List<Tag> filterTags(Annotation[] annotations) {
        return Stream.of(annotations)
                     .filter(annotation -> annotation.annotationType()
                                                     .isAnnotationPresent(IsTag.class))
                     .map(this::extract)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }
}
