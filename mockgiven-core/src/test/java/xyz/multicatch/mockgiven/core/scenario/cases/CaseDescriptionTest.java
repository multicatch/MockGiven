package xyz.multicatch.mockgiven.core.scenario.cases;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.CaseAs;

class CaseDescriptionTest {

    @DisplayName("CaseDescription should be correctly created")
    @Test
    void shouldCreateValueObject() {
        CaseAs caseAs = Mockito.mock(CaseAs.class);
        List values = Stream.of("a", "b").collect(Collectors.toList());
        Assertions.assertThat(new CaseDescription(caseAs, values))
                  .isInstanceOf(CaseDescription.class)
                  .extracting("caseAs", "values")
                  .containsOnly(caseAs, values);
    }

    @DisplayName("Value stored as CaseDescription should be retrieved correctly")
    @Test
    void shouldRetrieveValue() {
        CaseAs caseAs = Mockito.mock(CaseAs.class);
        List values = Stream.of("a", "b").collect(Collectors.toList());
        CaseDescription caseDescription = new CaseDescription(caseAs, values);

        Assertions.assertThat(caseDescription.getCaseAs())
                  .isEqualTo(caseAs);
        Assertions.assertThat(caseDescription.getValues())
                  .isEqualTo(values);
    }

    @DisplayName("CaseDescription should properly compare its value")
    @Test
    void shouldCompareObjects() {
        CaseAs caseAs = Mockito.mock(CaseAs.class);
        List values = Stream.of("a", "b").collect(Collectors.toList());
        List differentValues = Stream.of("a", "b", "c").collect(Collectors.toList());
        CaseDescription caseDescription = new CaseDescription(caseAs, values);
        CaseDescription caseDescription2 = new CaseDescription(caseAs, values);
        CaseDescription differentCaseDescription = new CaseDescription(caseAs, differentValues);

        Assertions.assertThat(caseDescription.equals(caseDescription2))
                  .isTrue();
        Assertions.assertThat(caseDescription.equals(differentCaseDescription))
                  .isFalse();
        Assertions.assertThat(caseDescription.equals("something else"))
                  .isFalse();
    }
}