package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.config.TagConfiguration;
import com.tngtech.jgiven.exception.JGivenWrongUsageException;
import com.tngtech.jgiven.impl.tag.DefaultTagDescriptionGenerator;
import com.tngtech.jgiven.impl.tag.DefaultTagHrefGenerator;
import com.tngtech.jgiven.report.model.Tag;

class AnnotationTagUtilsTest {

    @DisplayName("An empty list should be returned when tag config is empty")
    @Test
    void shouldReturnEmptyResultWhenTagConfigIsNull() {
        List<Tag> tags = AnnotationTagUtils.toTags(null, null);

        Assertions.assertThat(tags)
                  .isEmpty();
    }

    @DisplayName("An description should be created using given generator")
    @Test
    void shouldCreateDescription() {
        TagConfiguration tagConfiguration = Mockito.mock(TagConfiguration.class);
        Mockito.when(tagConfiguration.getDescriptionGenerator())
               .thenAnswer(invocation -> DefaultTagDescriptionGenerator.class);
        Mockito.when(tagConfiguration.getDescription())
               .thenReturn("another value");
        Annotation annotation = Mockito.mock(Annotation.class);
        String value = "value";

        String description = AnnotationTagUtils.getDescriptionFromGenerator(tagConfiguration, annotation, value);

        Assertions.assertThat(description)
                  .isEqualTo("another value");
    }

    @DisplayName("A JGiven exception should be thrown when a description generator cannot be instantiated")
    @Test
    void shouldThrowAJGivenExceptionOnDescriptionCreationError() {
        TagConfiguration tagConfiguration = Mockito.mock(TagConfiguration.class);
        Mockito.when(tagConfiguration.getDescriptionGenerator())
               .thenAnswer(invocation -> UnexpectedGenerator.class);
        Annotation annotation = Mockito.mock(Annotation.class);
        Mockito.when(annotation.toString())
               .thenReturn("Mock");
        String value = "value";

        Assertions.assertThatThrownBy(() ->
                AnnotationTagUtils.getDescriptionFromGenerator(tagConfiguration, annotation, value))
                  .isInstanceOf(JGivenWrongUsageException.class)
                  .hasMessage("Error while trying to generate the description for annotation Mock using DescriptionGenerator class class xyz.multicatch.mockgiven.core.annotations.tag.UnexpectedGenerator: xyz.multicatch.mockgiven.core.annotations.tag.UnexpectedGenerator. This exception indicates that you used JGiven in a wrong way. Please consult the JGiven documentation at http://jgiven.org/docs and the JGiven API documentation at http://jgiven.org/javadoc/ for further information.");
    }

    @DisplayName("An href should be created using given generator")
    @Test
    void shouldCreateHref() {
        TagConfiguration tagConfiguration = Mockito.mock(TagConfiguration.class);
        Mockito.when(tagConfiguration.getHrefGenerator())
               .thenAnswer(invocation -> DefaultTagHrefGenerator.class);
        Mockito.when(tagConfiguration.getHref())
               .thenReturn("another value");
        Annotation annotation = Mockito.mock(Annotation.class);
        String value = "value";

        String description = AnnotationTagUtils.getHref(tagConfiguration, annotation, value);

        Assertions.assertThat(description)
                  .isEqualTo("another value");
    }

    @DisplayName("A JGiven exception should be thrown when an href generator cannot be instantiated")
    @Test
    void shouldThrowAJGivenExceptionOnHrefCreationError() {
        TagConfiguration tagConfiguration = Mockito.mock(TagConfiguration.class);
        Mockito.when(tagConfiguration.getHrefGenerator())
               .thenAnswer(invocation -> UnexpectedGenerator.class);
        Annotation annotation = Mockito.mock(Annotation.class);
        Mockito.when(annotation.toString())
               .thenReturn("Mock");
        String value = "value";

        Assertions.assertThatThrownBy(() ->
                AnnotationTagUtils.getHref(tagConfiguration, annotation, value))
                  .isInstanceOf(JGivenWrongUsageException.class)
                  .hasMessage("Error while trying to generate the href for annotation Mock using HrefGenerator class class xyz.multicatch.mockgiven.core.annotations.tag.UnexpectedGenerator: xyz.multicatch.mockgiven.core.annotations.tag.UnexpectedGenerator. This exception indicates that you used JGiven in a wrong way. Please consult the JGiven documentation at http://jgiven.org/docs and the JGiven API documentation at http://jgiven.org/javadoc/ for further information.");
    }

    @DisplayName("Exploded tags list should be created")
    @Test
    void shouldCreateExplodedTags() {
        Tag originalTag = new Tag(
                "xyz.multicatch.mockgiven.core.annotations.tag.SampleTag",
                "MockedTag",
                "value"
        );

        String[] values = new String[2];
        values[0] = "abc";
        values[1] = "def";

        Annotation annotation = Mockito.mock(Annotation.class);

        TagConfiguration tagConfiguration = Mockito.mock(TagConfiguration.class);
        Mockito.when(tagConfiguration.getDescriptionGenerator())
               .thenAnswer(invocation -> DefaultTagDescriptionGenerator.class);
        Mockito.when(tagConfiguration.getDescription())
               .thenReturn("another value");
        Mockito.when(tagConfiguration.getHrefGenerator())
               .thenAnswer(invocation -> DefaultTagHrefGenerator.class);
        Mockito.when(tagConfiguration.getHref())
               .thenReturn("another value");

        List<Tag> explodedTags = AnnotationTagUtils.getExplodedTags(originalTag, values, annotation, tagConfiguration);

        Assertions.assertThat(explodedTags)
                  .hasSize(2);
        Assertions.assertThat(explodedTags.get(0))
                  .extracting(
                          "fullType",
                          "type",
                          "name",
                          "value",
                          "description",
                          "prependType",
                          "color",
                          "cssClass",
                          "style",
                          "tags",
                          "href",
                          "hideInNav"
                  )
                  .containsExactly(
                          "xyz.multicatch.mockgiven.core.annotations.tag.SampleTag",
                          null,
                          "MockedTag",
                          "abc",
                          "another value",
                          false,
                          null,
                          null,
                          null,
                          new ArrayList(),
                          "another value",
                          null
                  );
        Assertions.assertThat(explodedTags.get(1))
                  .extracting(
                          "fullType",
                          "type",
                          "name",
                          "value",
                          "description",
                          "prependType",
                          "color",
                          "cssClass",
                          "style",
                          "tags",
                          "href",
                          "hideInNav"
                  )
                  .containsExactly(
                          "xyz.multicatch.mockgiven.core.annotations.tag.SampleTag",
                          null,
                          "MockedTag",
                          "def",
                          "another value",
                          false,
                          null,
                          null,
                          null,
                          new ArrayList(),
                          "another value",
                          null
                  );
    }


    @DisplayName("Exploded tags list should be empty when there are no values")
    @Test
    void shouldReturnEmptyListOfExplodedTags() {
        Tag originalTag = new Tag(
                "xyz.multicatch.mockgiven.core.annotations.tag.SampleTag",
                "MockedTag",
                "value"
        );

        String[] values = new String[0];

        Annotation annotation = Mockito.mock(Annotation.class);
        TagConfiguration tagConfiguration = Mockito.mock(TagConfiguration.class);

        List<Tag> explodedTags = AnnotationTagUtils.getExplodedTags(originalTag, values, annotation, tagConfiguration);

        Assertions.assertThat(explodedTags)
                  .isEmpty();
    }
}