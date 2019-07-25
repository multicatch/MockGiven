package xyz.multicatch.mockgiven.core.annotations.caseas;

import com.tngtech.jgiven.annotation.CaseAs;
import com.tngtech.jgiven.annotation.CaseAsProvider;
import com.tngtech.jgiven.impl.util.ReflectionUtil;

public class CaseAsProviderFactory {
    public CaseAsProvider create(CaseAs caseAs) {
        if (caseAs != null) {
            return ReflectionUtil.newInstance(caseAs.provider());
        } else {
            return null;
        }
    }
}
