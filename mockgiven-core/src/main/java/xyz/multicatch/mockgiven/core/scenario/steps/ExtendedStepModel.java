package xyz.multicatch.mockgiven.core.scenario.steps;

import java.util.List;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import com.tngtech.jgiven.report.model.StepModel;
import com.tngtech.jgiven.report.model.StepStatus;

public class ExtendedStepModel extends StepModel {

    private static final Set<StepStatus> IGNORED_STATUSES_FOR_INHERITANCE = ImmutableSet.of(
            StepStatus.SKIPPED,
            StepStatus.PASSED
    );

    public void inheritStatusFromNested() {
        List<StepModel> nestedSteps = this.getNestedSteps();
        StepStatus status = StepStatus.PASSED;

        for (StepModel nestedModel : nestedSteps) {
            StepStatus nestedStatus = nestedModel.getStatus();

            if (!IGNORED_STATUSES_FOR_INHERITANCE.contains(nestedStatus)) {
                status = nestedStatus;
            }

            if (status == StepStatus.FAILED) {
                break;
            }
        }
        setStatus(status);
    }
}
