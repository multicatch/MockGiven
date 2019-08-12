package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tngtech.jgiven.config.TagConfiguration;
import com.tngtech.jgiven.exception.JGivenWrongUsageException;
import com.tngtech.jgiven.report.model.Tag;

public class AnnotationTagUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationTagUtils.class);

    private AnnotationTagUtils() {}

    public static List<Tag> toTags(
            TagConfiguration tagConfig,
            Annotation annotation
    ) {
        if (tagConfig == null) {
            return new ArrayList<>();
        }

        Tag tag = new Tag(tagConfig.getAnnotationFullType());

        tag.setType(tagConfig.getAnnotationType());

        if (!Strings.isNullOrEmpty(tagConfig.getName())) {
            tag.setName(tagConfig.getName());
        }

        if (tagConfig.isPrependType()) {
            tag.setPrependType(true);
        }

        tag.setShowInNavigation(tagConfig.showInNavigation());

        if (!Strings.isNullOrEmpty(tagConfig.getCssClass())) {
            tag.setCssClass(tagConfig.getCssClass());
        }

        if (!Strings.isNullOrEmpty(tagConfig.getColor())) {
            tag.setColor(tagConfig.getColor());
        }

        if (!Strings.isNullOrEmpty(tagConfig.getStyle())) {
            tag.setStyle(tagConfig.getStyle());
        }

        Object value = tagConfig.getDefaultValue();
        if (!Strings.isNullOrEmpty(tagConfig.getDefaultValue())) {
            tag.setValue(tagConfig.getDefaultValue());
        }

        tag.setTags(tagConfig.getTags());

        if (tagConfig.isIgnoreValue() || annotation == null) {
            tag.setDescription(AnnotationTagUtils.getDescriptionFromGenerator(tagConfig, annotation, tagConfig.getDefaultValue()));
            tag.setHref(AnnotationTagUtils.getHref(tagConfig, annotation, value));

            return Collections.singletonList(tag);
        }

        getStringValue(annotation).ifPresent(tag::setValue);
        getStringValueList(annotation).map(Collection::stream)
                                      .map(stream -> stream.map(String::valueOf).collect(Collectors.toList()))
                                      .ifPresent(tag::setValue);

        if (!tag.getValues().isEmpty() && tagConfig.isExplodeArray()) {
            return AnnotationTagUtils.getExplodedTags(tag, tag.getValues().toArray(), annotation, tagConfig);
        }

        tag.setDescription(AnnotationTagUtils.getDescriptionFromGenerator(tagConfig, annotation, tag.getValueString()));
        tag.setHref(AnnotationTagUtils.getHref(tagConfig, annotation, tag.getValueString()));

        return Collections.singletonList(tag);
    }

    private static Optional<String> getStringValue(Annotation annotation) {
        try {
            Method method = annotation.annotationType()
                                      .getMethod("value");

            if (method.getReturnType() == String.class) {
                return Optional.ofNullable((String) method.invoke(annotation));
            }
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Error while getting String 'value' method of annotation " + annotation, e);
        }

        return Optional.empty();
    }

    private static Optional<List<Object>> getStringValueList(Annotation annotation) {
        try {
            Method method = annotation.annotationType()
                                      .getMethod("value");

            Object value = method.invoke(annotation);

            if (value != null) {
                if (value.getClass()
                         .isArray()) {
                    Object[] objectArray = (Object[]) value;
                    return Optional.of(Arrays.asList(objectArray));
                }
            }
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Error while getting array 'value' method of annotation " + annotation, e);
        }

        return Optional.empty();
    }

    public static String getDescriptionFromGenerator(
            TagConfiguration tagConfiguration,
            Annotation annotation,
            Object value
    ) {
        try {
            return tagConfiguration.getDescriptionGenerator()
                                   .newInstance()
                                   .generateDescription(tagConfiguration, annotation, value);
        } catch (Exception e) {
            throw new JGivenWrongUsageException(
                    "Error while trying to generate the description for annotation " + annotation + " using DescriptionGenerator class "
                            + tagConfiguration.getDescriptionGenerator() + ": " + e.getMessage(),
                    e);
        }
    }

    public static String getHref(
            TagConfiguration tagConfiguration,
            Annotation annotation,
            Object value
    ) {
        try {
            return tagConfiguration.getHrefGenerator()
                                   .newInstance()
                                   .generateHref(tagConfiguration, annotation, value);
        } catch (Exception e) {
            throw new JGivenWrongUsageException(
                    "Error while trying to generate the href for annotation " + annotation + " using HrefGenerator class "
                            + tagConfiguration.getHrefGenerator() + ": " + e.getMessage(),
                    e);
        }
    }

    public static List<Tag> getExplodedTags(
            Tag originalTag,
            Object[] values,
            Annotation annotation,
            TagConfiguration tagConfig
    ) {
        List<Tag> result = Lists.newArrayList();
        for (Object singleValue : values) {
            Tag newTag = originalTag.copy();
            newTag.setValue(String.valueOf(singleValue));
            newTag.setDescription(getDescriptionFromGenerator(tagConfig, annotation, singleValue));
            newTag.setHref(getHref(tagConfig, annotation, singleValue));
            result.add(newTag);
        }
        return result;
    }
}
