package xyz.multicatch.mockgiven.core.scenario.steps;

import java.util.List;
import com.tngtech.jgiven.exception.JGivenWrongUsageException;
import com.tngtech.jgiven.report.model.NamedArgument;

public class StepCommentFactory {

    public String create(List<NamedArgument> arguments) {
        if (arguments == null || arguments.size() != 1) {
            throw new JGivenWrongUsageException("A step comment method must have exactly one parameter.");
        }

        if (!(arguments.get(0)
                       .getValue() instanceof String)) {
            throw new JGivenWrongUsageException("The step comment method parameter must be a string.");
        }

        return (String) arguments.get(0)
                                 .getValue();
    }
}
