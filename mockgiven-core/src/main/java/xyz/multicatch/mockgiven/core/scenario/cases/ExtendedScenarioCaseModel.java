package xyz.multicatch.mockgiven.core.scenario.cases;

import java.util.List;
import com.tngtech.jgiven.report.model.ExecutionStatus;
import com.tngtech.jgiven.report.model.ScenarioCaseModel;

public class ExtendedScenarioCaseModel extends ScenarioCaseModel {

    public void setException(Throwable throwable, List<String> stackTrace) {
        this.setSuccess(false);
        this.setStatus(ExecutionStatus.FAILED);
        this.setErrorMessage(throwable.getClass().getName() + ": " + throwable.getMessage());
        this.setStackTrace(stackTrace);
    }

    public boolean isFirstCase() {
        return getCaseNr() == 1;
    }
}
