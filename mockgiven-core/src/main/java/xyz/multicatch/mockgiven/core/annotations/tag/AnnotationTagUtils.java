package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tngtech.jgiven.config.TagConfiguration;
import com.tngtech.jgiven.exception.JGivenWrongUsageException;
import com.tngtech.jgiven.report.model.Tag;
import xyz.multicatch.mockgiven.core.utils.ObjectUtils;

public class AnnotationTagUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationTagUtils.class);

    private AnnotationTagUtils() {}

    public static List<Tag> toTags(
            TagConfiguration tagConfig,
            Optional<Annotation> annotation
    ) {
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

        if (tagConfig.isIgnoreValue() || !annotation.isPresent()) {
            tag.setDescription(AnnotationTagUtils.getDescriptionFromGenerator(tagConfig, annotation.orElse(null), tagConfig.getDefaultValue()));
            tag.setHref(AnnotationTagUtils.getHref(tagConfig, annotation.orElse(null), value));

            return Arrays.asList(tag);
        }

        try {
            Method method = annotation.get()
                                      .annotationType()
                                      .getMethod("value");
            value = method.invoke(annotation.get());
            if (value != null) {
                if (value.getClass()
                         .isArray()) {
                    Object[] objectArray = (Object[]) value;
                    if (tagConfig.isExplodeArray()) {
                        List<Tag> explodedTags = AnnotationTagUtils.getExplodedTags(tag, objectArray, annotation.get(), tagConfig);
                        return explodedTags;
                    }
                    tag.setValue(ObjectUtils.toList(objectArray));
                } else {
                    tag.setValue(String.valueOf(value));
                }
            }
        } catch (NoSuchMethodException ignore) {

        } catch (Exception e) {
            LOGGER.error("Error while getting 'value' method of annotation " + annotation.get(), e);
        }

        tag.setDescription(AnnotationTagUtils.getDescriptionFromGenerator(tagConfig, annotation.get(), value));
        tag.setHref(AnnotationTagUtils.getHref(tagConfig, annotation.get(), value));

        return Arrays.asList(tag);
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
