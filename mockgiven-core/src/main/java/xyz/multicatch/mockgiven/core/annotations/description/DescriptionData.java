package xyz.multicatch.mockgiven.core.annotations.description;

import java.util.List;
import com.tngtech.jgiven.report.model.Word;
import lombok.Value;
import xyz.multicatch.mockgiven.core.scenario.steps.ExtendedStepModel;

@Value
public class DescriptionData {
    String name;
    List<Word> words;

    public static DescriptionData of(ExtendedStepModel stepModel) {
        return new DescriptionData(stepModel.getName(), stepModel.getWords());
    }
}
