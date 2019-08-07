package xyz.multicatch.mockgiven.core.annotations.caseas;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.CaseAs;
import com.tngtech.jgiven.annotation.CaseAsProvider;

class CaseAsProviderFactoryTest {
    private CaseAsProviderFactory caseAsProviderFactory = new CaseAsProviderFactory();

    @DisplayName("Null should be returned when CaseAs is null")
    @Test
    void shouldNotCreateCaseAs() {
        CaseAsProvider caseAsProvider = caseAsProviderFactory.create(null);
        Assertions.assertThat(caseAsProvider)
                  .isNull();
    }


    @DisplayName("A given CaseAsProvider should be created")
    @Test
    void shouldCreateCaseAs() {
        CaseAs caseAs = Mockito.mock(CaseAs.class);
        Mockito.when(caseAs.provider())
               .thenAnswer(invocation -> MockedCaseAsProvider.class);

        CaseAsProvider caseAsProvider = caseAsProviderFactory.create(caseAs);
        Assertions.assertThat(caseAsProvider)
                  .isInstanceOf(MockedCaseAsProvider.class);
    }
}