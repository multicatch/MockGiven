package xyz.multicatch.mockgiven.core.annotations.description;

import static xyz.multicatch.mockgiven.core.annotations.description.WordUtils.wordsOf;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.tngtech.jgiven.report.model.Word;

class DescriptionQueueTest {
    private DescriptionQueue descriptionQueue = new DescriptionQueue();

    @DisplayName("Description queue should return the same data as added")
    @Test
    void shouldCreateSimpleDescription() {
        String methodName = "test method";
        DescriptionData descriptionData = new DescriptionData(methodName, wordsOf(methodName));

        descriptionQueue.add(descriptionData);
        DescriptionData result = descriptionQueue.join();

        Assertions.assertThat(result)
                  .isNotNull();

        String expectedName = "test method";
        Assertions.assertThat(result.getName())
                  .isEqualTo(expectedName);

        Assertions.assertThat(result.getWords())
                  .containsExactly(wordsOf(expectedName).toArray(new Word[0]));
    }

    @DisplayName("Joint description should be properly created from everything that is queued")
    @Test
    void shouldCreateJointDescription() {
        String methodName = "test method";
        DescriptionData descriptionData = new DescriptionData(methodName, wordsOf(methodName));

        String secondMethodName = "another one";
        DescriptionData secondDescriptionData = new DescriptionData(secondMethodName, wordsOf(secondMethodName));

        descriptionQueue.add(descriptionData);
        descriptionQueue.add(secondDescriptionData);
        DescriptionData result = descriptionQueue.join();

        Assertions.assertThat(result)
                  .isNotNull();

        String expectedName = "test method another one";
        Assertions.assertThat(result.getName())
                  .isEqualTo(expectedName);

        Assertions.assertThat(result.getWords())
                  .containsExactly(wordsOf(expectedName).toArray(new Word[0]));
    }

    @DisplayName("Description queue should return null when empty")
    @Test
    void shouldReturnNullWhenEmpty() {
        DescriptionData result = descriptionQueue.join();

        Assertions.assertThat(result)
                  .isNull();
    }
}