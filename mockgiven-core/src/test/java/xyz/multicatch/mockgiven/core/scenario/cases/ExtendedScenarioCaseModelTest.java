package xyz.multicatch.mockgiven.core.scenario.cases;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.tngtech.jgiven.report.model.ExecutionStatus;

class ExtendedScenarioCaseModelTest {

    @DisplayName("Case model should correctly tell if it's first case or not")
    @Test
    void shouldTellIfFirstCase() {
        ExtendedScenarioCaseModel caseModel = new ExtendedScenarioCaseModel();
        caseModel.setCaseNr(12);
        Assertions.assertThat(caseModel.isFirstCase())
                  .isFalse();

        caseModel.setCaseNr(1);
        Assertions.assertThat(caseModel.isFirstCase())
                  .isTrue();
    }

    @DisplayName("An exception should trigger failed state of case")
    @Test
    void shouldSetException() {
        ExtendedScenarioCaseModel caseModel = new ExtendedScenarioCaseModel();
        IllegalStateException exception = new IllegalStateException("message");
        List<String> stackTrace = Stream.of("xyz.multicatch.mockgiven.core.scenario.cases.ScenarioCaseModel", "xyz.multicatch.mockgiven.core.scenario.cases.ExtendedScenarioCaseModelTest")
                .collect(Collectors.toList());

        caseModel.setException(exception, stackTrace);
        Assertions.assertThat(caseModel.isSuccess())
                  .isFalse();
        Assertions.assertThat(caseModel.getExecutionStatus())
                  .isEqualTo(ExecutionStatus.FAILED);
        Assertions.assertThat(caseModel.getErrorMessage())
                  .isEqualTo("java.lang.IllegalStateException: message");
        Assertions.assertThat(caseModel.getStackTrace())
                  .isEqualTo(stackTrace);
    }
}