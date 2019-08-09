package xyz.multicatch.mockgiven.core.annotations.description;

import static xyz.multicatch.mockgiven.core.annotations.description.WordUtils.wordsOf;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.report.model.Word;
import xyz.multicatch.mockgiven.core.scenario.steps.ExtendedStepModel;

class DescriptionDataTest {

    @DisplayName("Description Data should be created from ExtendedStepModel")
    @Test
    void shouldCreateDescriptionData() {
        ExtendedStepModel stepModel = Mockito.mock(ExtendedStepModel.class);
        Mockito.when(stepModel.getName())
               .thenReturn("test model");
        Mockito.when(stepModel.getWords())
               .thenReturn(wordsOf("test model"));

        DescriptionData descriptionData = DescriptionData.of(stepModel);

        Assertions.assertThat(descriptionData)
                  .isNotNull();
        Assertions.assertThat(descriptionData.getName())
                  .isEqualTo("test model");
        Assertions.assertThat(descriptionData.getWords())
                  .containsExactly(wordsOf("test model").toArray(new Word[0]));
    }

}