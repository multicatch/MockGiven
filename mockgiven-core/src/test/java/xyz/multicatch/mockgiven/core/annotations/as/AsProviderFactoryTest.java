package xyz.multicatch.mockgiven.core.annotations.as;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.AsProvider;
import com.tngtech.jgiven.impl.params.DefaultAsProvider;

class AsProviderFactoryTest {

    private AsProviderFactory asProviderFactory = new AsProviderFactory();

    @DisplayName("A concrete CaseAsProvider should be created from annotation")
    @Test
    void shouldCreateAsProviderFromAnnotation() {
        As as = Mockito.mock(As.class);

        Mockito.when(as.provider())
               .thenAnswer(invocation -> MockedAsProvider.class);

        AsProvider asProvider = asProviderFactory.create(as);

        Assertions.assertThat(asProvider)
                  .isInstanceOf(MockedAsProvider.class);
    }

    @DisplayName("A default CaseAsProvider should be created when annotation is not present")
    @Test
    void shouldCreateDefaultAsProvider() {
        AsProvider asProvider = asProviderFactory.create(null);

        Assertions.assertThat(asProvider)
                  .isInstanceOf(DefaultAsProvider.class);
    }
}