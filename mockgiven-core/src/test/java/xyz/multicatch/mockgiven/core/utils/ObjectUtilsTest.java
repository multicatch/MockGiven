package xyz.multicatch.mockgiven.core.utils;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ObjectUtilsTest {

    @DisplayName("A string list should be created from object array")
    @Test
    void shouldCreateStringList() {
        Object[] givenObjects = new Object[3];
        givenObjects[0] = null;
        givenObjects[1] = Mockito.mock(Object.class);
        Mockito.when(givenObjects[1].toString())
               .thenReturn("test");
        givenObjects[2] = "abcdefghij";

        List<String> strings = ObjectUtils.toList(givenObjects);

        Assertions.assertThat(strings)
                  .containsExactly("null", "test", "abcdefghij");
    }

}