package xyz.multicatch.mockgiven.core.scenario.cases;

import java.lang.reflect.Method;
import java.util.List;
import com.tngtech.jgiven.annotation.CaseAs;
import com.tngtech.jgiven.annotation.CaseAsProvider;
import com.tngtech.jgiven.report.model.NamedArgument;
import com.tngtech.jgiven.report.model.ScenarioCaseModel;
import xyz.multicatch.mockgiven.core.annotations.caseas.CaseAsFactory;
import xyz.multicatch.mockgiven.core.annotations.caseas.CaseAsProviderFactory;
import xyz.multicatch.mockgiven.core.scenario.methods.arguments.ArgumentUtils;

public class CaseDescriptionFactory {
    private final CaseAsFactory caseAsFactory;
    private final CaseAsProviderFactory caseAsProviderFactory;

    public CaseDescriptionFactory(
            CaseAsFactory caseAsFactory,
            CaseAsProviderFactory caseAsProviderFactory
    ) {
        this.caseAsFactory = caseAsFactory;
        this.caseAsProviderFactory = caseAsProviderFactory;
    }

    public CaseDescription create(
            Method method,
            Class testClass,
            ScenarioCaseModel scenarioCaseModel,
            List<NamedArgument> namedArguments
    ) {
        CaseAs caseAs = caseAsFactory.create(method, testClass);

        if (caseAs != null) {
            List<?> values;
            if (caseAs.formatValues()) {
                values = scenarioCaseModel.getExplicitArguments();
            } else {
                values = ArgumentUtils.getValues(namedArguments);
            }
            return new CaseDescription(caseAs, values);
        }

        return null;
    }

    public String create(CaseDescription caseDescription, List<String> parameterNames) {
        CaseAsProvider caseAsProvider = caseAsProviderFactory.create(caseDescription.getCaseAs());
        return caseAsProvider.as(caseDescription.getCaseAs().value(), parameterNames, caseDescription.getValues());
    }
}
