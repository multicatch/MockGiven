package xyz.multicatch.mockgiven.core.annotations.caseas;

import java.lang.reflect.Method;
import com.tngtech.jgiven.annotation.CaseAs;

public class CaseAsFactory {
    public CaseAs create(Method method, Class testClass) {
        CaseAs caseAs = null;
        if (method.isAnnotationPresent(CaseAs.class)) {
            caseAs = method.getAnnotation(CaseAs.class);
        } else if (testClass.isAnnotationPresent(CaseAs.class)) {
            caseAs = (CaseAs) testClass.getAnnotation(CaseAs.class);
        }

        return caseAs;
    }
}
