package xyz.multicatch.mockgiven.core.annotations.description;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        DescriptionData descriptionData = new DescriptionData(methodName, words(methodName));

        descriptionQueue.add(descriptionData);
        DescriptionData result = descriptionQueue.join();

        Assertions.assertThat(result)
                  .isNotNull();

        String expectedName = "test method";
        Assertions.assertThat(result.getName())
                  .isEqualTo(expectedName);

        Assertions.assertThat(result.getWords())
                  .containsExactly(words(expectedName).toArray(new Word[0]));
    }

    @DisplayName("Joint description should be properly created from everything that is queued")
    @Test
    void shouldCreateJointDescription() {
        String methodName = "test method";
        DescriptionData descriptionData = new DescriptionData(methodName, words(methodName));

        String secondMethodName = "another one";
        DescriptionData secondDescriptionData = new DescriptionData(secondMethodName, words(secondMethodName));

        descriptionQueue.add(descriptionData);
        descriptionQueue.add(secondDescriptionData);
        DescriptionData result = descriptionQueue.join();

        Assertions.assertThat(result)
                  .isNotNull();

        String expectedName = "test method another one";
        Assertions.assertThat(result.getName())
                  .isEqualTo(expectedName);

        Assertions.assertThat(result.getWords())
                  .containsExactly(words(expectedName).toArray(new Word[0]));
    }

    @DisplayName("Description queue should return null when empty")
    @Test
    void shouldReturnNullWhenEmpty() {
        DescriptionData result = descriptionQueue.join();

        Assertions.assertThat(result)
                  .isNull();
    }

    private List<Word> words(String input) {
        return Stream.of(input.split(" "))
                     .map(Word::new)
                     .collect(Collectors.toList());
    }

}