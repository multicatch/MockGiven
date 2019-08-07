package xyz.multicatch.mockgiven.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExceptionUtils {
    private ExceptionUtils() {}

    public static List<String> getStackTrace(
            Throwable exception
    ) {
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        ArrayList<String> stackTrace = new ArrayList<>(stackTraceElements.length);

        for (StackTraceElement element : stackTraceElements) {
            stackTrace.add(element.toString());
        }
        return stackTrace;
    }

    public static List<String> getFilteredStackTrace(
            Throwable exception,
            Set<String> stackTraceFilter
    ) {
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        ArrayList<String> stackTrace = new ArrayList<>(stackTraceElements.length);

        for (StackTraceElement element : stackTraceElements) {
            boolean isInFilter = stackTraceFilter.stream()
                                                 .anyMatch(blacklisted -> element.getClassName()
                                                                                 .contains(blacklisted));

            if (!isInFilter) {
                stackTrace.add(element.toString());
            }
        }
        return stackTrace;
    }
}
