package xyz.multicatch.mockgiven.core.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExceptionUtilsTest {

    @DisplayName("A stack trace should be created from exception")
    @Test
    void shouldReturnStackTrace() {
        List<StackTraceElement> stackTrace = new ArrayList<>();
        stackTrace.add(stackTraceElement("java.util.Something"));
        stackTrace.add(stackTraceElement("xyz.multicatch.mockgiven.core.MockGiven"));

        Exception exception = Mockito.mock(Exception.class);
        Mockito.when(exception.getStackTrace())
               .thenReturn(stackTrace.toArray(new StackTraceElement[0]));

        List<String> actualStackTrace = ExceptionUtils.getStackTrace(exception);

        Assertions.assertThat(actualStackTrace)
                  .containsExactly("java.util.Something", "xyz.multicatch.mockgiven.core.MockGiven");
    }

    @DisplayName("A filtered stack trace should be created from exception")
    @Test
    void shouldReturnFilteredStackTrace() {
        List<StackTraceElement> stackTrace = new ArrayList<>();
        stackTrace.add(stackTraceElement("java.util.Something"));
        stackTrace.add(stackTraceElement("xyz.multicatch.mockgiven.core.MockGiven"));

        Set<String> stackTraceFilter = new HashSet<>();
        stackTraceFilter.add("java.util");

        Exception exception = Mockito.mock(Exception.class);
        Mockito.when(exception.getStackTrace())
               .thenReturn(stackTrace.toArray(new StackTraceElement[0]));

        List<String> actualStackTrace = ExceptionUtils.getFilteredStackTrace(exception, stackTraceFilter);

        Assertions.assertThat(actualStackTrace)
                  .containsExactly("xyz.multicatch.mockgiven.core.MockGiven");
    }

    StackTraceElement stackTraceElement(String className) {
        StackTraceElement mock = Mockito.mock(StackTraceElement.class);
        Mockito.when(mock.toString())
               .thenReturn(className);
        Mockito.when(mock.getClassName())
               .thenReturn(className);
        return mock;
    }

}