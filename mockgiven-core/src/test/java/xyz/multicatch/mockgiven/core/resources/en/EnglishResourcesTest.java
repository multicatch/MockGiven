package xyz.multicatch.mockgiven.core.resources.en;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.multicatch.mockgiven.core.resources.TextResource;

class EnglishResourcesTest {

    private EnglishResources englishResources = new EnglishResources();

    @DisplayName("English resources should be present for every supported resource")
    @Test
    void shouldHaveLocalizationForResources() {
        for (TextResource resource : TextResource.values()) {
            Assertions.assertThat(englishResources.get(resource))
                      .isNotBlank();
        }
    }

}