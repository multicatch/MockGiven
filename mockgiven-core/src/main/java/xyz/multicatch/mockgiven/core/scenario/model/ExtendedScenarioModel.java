package xyz.multicatch.mockgiven.core.scenario.model;

import java.util.List;
import java.util.stream.Collectors;
import com.tngtech.jgiven.impl.util.WordUtil;
import com.tngtech.jgiven.report.model.ScenarioModel;

public class ExtendedScenarioModel extends ScenarioModel {

    public void setExplicitParametersWithoutUnderline(List<String> parameterNames) {
        super.setExplicitParameters(parameterNames.stream()
                                                  .map(WordUtil::fromSnakeCase)
                                                  .collect(Collectors.toList()));
    }
}
