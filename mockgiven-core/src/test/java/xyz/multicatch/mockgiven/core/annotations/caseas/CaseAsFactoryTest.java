package xyz.multicatch.mockgiven.core.annotations.caseas;

import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.CaseAs;

class CaseAsFactoryTest {
    private CaseAsFactory caseAsFactory = new CaseAsFactory();

    @DisplayName("Factory should create CaseAs type when such annotation is encountered on a given method")
    @Test
    void shouldCreateCaseAsFromMethod() {
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.isAnnotationPresent(Mockito.eq(CaseAs.class)))
               .thenReturn(true);
        CaseAs mockedCaseAs = mockedCaseAs();
        Mockito.when(method.getAnnotation(Mockito.eq(CaseAs.class)))
               .thenReturn(mockedCaseAs);

        CaseAs caseAs = caseAsFactory.create(method, PlainClass.class);

        Assertions.assertThat(caseAs)
                  .isEqualTo(mockedCaseAs);
    }

    @DisplayName("Factory should create CaseAs type when such annotation is encountered on a given test class")
    @Test
    void shouldCreateCaseAsFromClass() {
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.isAnnotationPresent(Mockito.eq(CaseAs.class)))
               .thenReturn(false);

        CaseAs caseAs = caseAsFactory.create(method, AnnotatedClass.class);

        Assertions.assertThat(caseAs)
                  .isEqualTo(AnnotatedClass.class.getAnnotation(CaseAs.class));
    }


    @DisplayName("Factory should create CaseAs type from method even though a test class also has such annotation")
    @Test
    void shouldCaseAsFromMethodHaveHigherPriority() {
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.isAnnotationPresent(Mockito.eq(CaseAs.class)))
               .thenReturn(true);
        CaseAs mockedCaseAs = mockedCaseAs();
        Mockito.when(method.getAnnotation(Mockito.eq(CaseAs.class)))
               .thenReturn(mockedCaseAs);

        CaseAs caseAs = caseAsFactory.create(method, PlainClass.class);

        Assertions.assertThat(caseAs)
                  .isEqualTo(mockedCaseAs)
                  .isNotEqualTo(AnnotatedClass.class.getAnnotation(CaseAs.class));
    }

    @DisplayName("Factory should return null when CaseAs annotation is not present")
    @Test
    void shouldNotCreateCaseAs() {
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.isAnnotationPresent(Mockito.eq(CaseAs.class)))
               .thenReturn(false);

        CaseAs caseAs = caseAsFactory.create(method, PlainClass.class);

        Assertions.assertThat(caseAs)
                  .isNull();
    }

    private CaseAs mockedCaseAs() {
        return Mockito.mock(CaseAs.class);
    }

    class PlainClass {
    }

    @CaseAs
    class AnnotatedClass {
    }

}