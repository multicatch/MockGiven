package xyz.multicatch.mockgiven.core.annotations.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.tngtech.jgiven.config.AbstractJGivenConfiguration;
import com.tngtech.jgiven.config.TagConfiguration;
import com.tngtech.jgiven.report.model.Tag;

class AnnotationTagExtractorTest {

    @Mock
    private AbstractJGivenConfiguration abstractJGivenConfiguration;

    @InjectMocks
    private AnnotationTagExtractor annotationTagExtractor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("An empty list should be returned when annotation is not a tag")
    @Test
    void shouldReturnEmptyListIfNoTagIsPresent() {
        DummyAnnotation dummyAnnotation = Mockito.mock(DummyAnnotation.class);
        Mockito.when(dummyAnnotation.annotationType())
               .thenAnswer(invocation -> DummyAnnotation.class);

        List<Tag> result = annotationTagExtractor.extract(dummyAnnotation);

        Assertions.assertThat(result)
                  .isEmpty();
    }

    @DisplayName("A tag should be returned when annotation is configured as a tag, but not annotated")
    @Test
    void shouldReturnTagsFromConfigIfNoTagIsPresent() {
        TagConfiguration tagConfiguration = new TagConfiguration(SampleTag.class);

        Mockito.when(abstractJGivenConfiguration.getTagConfiguration(Mockito.any()))
               .thenReturn(tagConfiguration);

        DummyAnnotation dummyAnnotation = Mockito.mock(DummyAnnotation.class);
        Mockito.when(dummyAnnotation.annotationType())
               .thenAnswer(invocation -> DummyAnnotation.class);

        List<Tag> result = annotationTagExtractor.extract(dummyAnnotation);

        Assertions.assertThat(result)
                  .hasSize(1);
        Assertions.assertThat(result.get(0))
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
                          "SampleTag",
                          "SampleTag",
                          null,
                          "",
                          false,
                          null,
                          null,
                          null,
                          new ArrayList(),
                          "",
                          null
                  );
    }

    @DisplayName("A tag metadata should be extracted from annotation")
    @Test
    void shouldExtractTag() {
        SampleTag annotation = Mockito.mock(SampleTag.class);
        Mockito.when(annotation.annotationType())
               .thenAnswer(invocation -> SampleTag.class);

        List<Tag> result = annotationTagExtractor.extract(annotation);

        Assertions.assertThat(result)
                  .hasSize(1);
        Assertions.assertThat(result.get(0))
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
                          "SampleTag",
                          "Tag name",
                          "Tag value",
                          "Sophisticated description",
                          true,
                          "blue",
                          "tag-class",
                          "nice",
                          new ArrayList(),
                          "http://multicatch.xyz",
                          true
                  );
    }

    @DisplayName("A tag metadata should be extracted with an inherited tag")
    @Test
    void shouldExtractTagWithInheritedTags() {
        SophisticatedTag annotation = Mockito.mock(SophisticatedTag.class);
        Mockito.when(annotation.annotationType())
               .thenAnswer(invocation -> SophisticatedTag.class);

        Mockito.when(annotation.value())
               .thenReturn(new String[]{ "a", "b", "c" });

        List<Tag> result = annotationTagExtractor.extract(annotation);

        Assertions.assertThat(result)
                  .hasSize(3);
        Assertions.assertThat(result.get(0))
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
                          "xyz.multicatch.mockgiven.core.annotations.tag.SophisticatedTag",
                          "SophisticatedTag",
                          "Sophisticated tag",
                          "a",
                          "",
                          false,
                          null,
                          null,
                          null,
                          Collections.singletonList("xyz.multicatch.mockgiven.core.annotations.tag.SampleTag-Tag value"),
                          "",
                          null
                  );

        Assertions.assertThat(result.get(1))
                  .extracting("value")
                  .contains("b");

        Assertions.assertThat(result.get(2))
                  .extracting("value")
                  .contains("c");
    }

    @DisplayName("A tag extractor should be created from given config")
    @Test
    void shouldCreateTagExtractor() {
        AnnotationTagExtractor annotationTagExtractor = AnnotationTagExtractor.forConfig(abstractJGivenConfiguration);
        Assertions.assertThat(annotationTagExtractor)
                  .extracting("configuration")
                  .containsExactly(abstractJGivenConfiguration);
    }
}