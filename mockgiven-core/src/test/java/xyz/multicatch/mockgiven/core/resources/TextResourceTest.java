package xyz.multicatch.mockgiven.core.resources;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TextResourceTest {

    @DisplayName("Resource declarations should be present")
    @Test
    void shouldContainResourceDeclarations() {
        Assertions.assertThat(TextResource.values())
                  .containsExactly(TextResource.GIVEN, TextResource.WHEN, TextResource.THEN, TextResource.AND, TextResource.BUT, TextResource.WITH);
    }
}