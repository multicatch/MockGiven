package xyz.multicatch.mockgiven.core.scenario.cases;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.tngtech.jgiven.annotation.CaseAs;
import com.tngtech.jgiven.report.model.NamedArgument;
import com.tngtech.jgiven.report.model.ScenarioCaseModel;
import xyz.multicatch.mockgiven.core.annotations.caseas.CaseAsFactory;
import xyz.multicatch.mockgiven.core.annotations.caseas.CaseAsProviderFactory;
import xyz.multicatch.mockgiven.core.annotations.caseas.MockedCaseAsProvider;

class CaseDescriptionFactoryTest {

    @Mock
    private CaseAsFactory caseAsFactory;

    @Mock
    private CaseAsProviderFactory caseAsProviderFactory;

    @InjectMocks
    private CaseDescriptionFactory caseDescriptionFactory;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnNullWhenCaseAsIsAbsent() {
        Method method = Mockito.mock(Method.class);
        Class testClass = this.getClass();
        ScenarioCaseModel scenarioCaseModel = new ScenarioCaseModel();

        CaseDescription caseDescription = caseDescriptionFactory.create(method, testClass, scenarioCaseModel, new ArrayList<>());

        Assertions.assertThat(caseDescription)
                  .isNull();
    }

    @Test
    void shouldCreateDescriptionUsingArguments() {
        CaseAs caseAs = Mockito.mock(CaseAs.class);
        Mockito.when(caseAs.formatValues())
               .thenReturn(false);

        Mockito.when(caseAsFactory.create(Mockito.any(), Mockito.any()))
               .thenReturn(caseAs);

        Method method = Mockito.mock(Method.class);
        Class testClass = this.getClass();
        ScenarioCaseModel scenarioCaseModel = new ScenarioCaseModel();
        ArrayList<NamedArgument> namedArguments = new ArrayList<>();
        namedArguments.add(new NamedArgument("test", "value"));
        namedArguments.add(new NamedArgument("test2", "value2"));

        CaseDescription caseDescription = caseDescriptionFactory.create(method, testClass, scenarioCaseModel, namedArguments);

        Assertions.assertThat(caseDescription)
                  .isNotNull();
        Assertions.assertThat(caseDescription.getCaseAs())
                  .isEqualTo(caseAs);
        Assertions.assertThat(caseDescription.getValues())
                  .containsExactly("value", "value2");
    }

    @Test
    void shouldCreateDescriptionUsingExplicitScenarioCaseArguments() {
        CaseAs caseAs = Mockito.mock(CaseAs.class);
        Mockito.when(caseAs.formatValues())
               .thenReturn(true);

        Mockito.when(caseAsFactory.create(Mockito.any(), Mockito.any()))
               .thenReturn(caseAs);

        Method method = Mockito.mock(Method.class);
        Class testClass = this.getClass();
        ScenarioCaseModel scenarioCaseModel = new ScenarioCaseModel();
        scenarioCaseModel.setExplicitArguments(Arrays.asList("a", "b", "c"));

        ArrayList<NamedArgument> namedArguments = new ArrayList<>();
        namedArguments.add(new NamedArgument("test", "value"));
        namedArguments.add(new NamedArgument("test2", "value2"));

        CaseDescription caseDescription = caseDescriptionFactory.create(method, testClass, scenarioCaseModel, namedArguments);

        Assertions.assertThat(caseDescription)
                  .isNotNull();
        Assertions.assertThat(caseDescription.getCaseAs())
                  .isEqualTo(caseAs);
        Assertions.assertThat(caseDescription.getValues())
                  .containsExactly("a", "b", "c");
    }

    @Test
    void shouldCreateDescriptionUsingCaseAsProvider() {
        Mockito.when(caseAsProviderFactory.create(Mockito.any()))
               .thenReturn(new MockedCaseAsProvider());

        CaseAs caseAs = Mockito.mock(CaseAs.class);
        Mockito.when(caseAs.value())
               .thenReturn("value");
        CaseDescription caseDescription = new CaseDescription(caseAs, Arrays.asList("test", "test2"));
        String description = caseDescriptionFactory.create(caseDescription, Arrays.asList("a", "b", "c"));

        Assertions.assertThat(description)
                  .isEqualTo("value;a,b,c;test,test2");
    }
}