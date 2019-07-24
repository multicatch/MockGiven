package xyz.multicatch.mockgiven.core.scenario;

import java.lang.reflect.Method;
import java.util.List;
import com.tngtech.jgiven.impl.ScenarioBase;
import com.tngtech.jgiven.impl.ScenarioModelBuilder;
import com.tngtech.jgiven.report.model.NamedArgument;
import com.tngtech.jgiven.report.model.ReportModel;
import com.tngtech.jgiven.report.model.ScenarioCaseModel;
import com.tngtech.jgiven.report.model.ScenarioModel;
import xyz.multicatch.mockgiven.core.scenario.creator.ByteBuddyStageClassCreator;
import xyz.multicatch.mockgiven.core.scenario.model.MockScenarioModelBuilder;

public class MockScenarioBase extends ScenarioBase {
    protected final ScenarioModelBuilder modelBuilder = new MockScenarioModelBuilder(getExecutor());
    private boolean initialized = false;

    public MockScenarioBase() {
        MockScenarioExecutor scenarioExecutor = new MockScenarioExecutor();
        scenarioExecutor.setStageClassCreator(new ByteBuddyStageClassCreator());
        setExecutor(scenarioExecutor);
    }

    public void setModel(ReportModel reportModel) {
        assertNotInitialized();
        modelBuilder.setReportModel(reportModel);
    }

    public ScenarioModel getScenarioModel() {
        return modelBuilder.getScenarioModel();
    }

    public ScenarioCaseModel getScenarioCaseModel() {
        return modelBuilder.getScenarioCaseModel();
    }

    public ReportModel getModel() {
        return modelBuilder.getReportModel();
    }

    public ScenarioBase startScenario(
            Class<?> testClass,
            Method method,
            List<NamedArgument> arguments
    ) {
        performInitialization();
        executor.startScenario(testClass, method, arguments);
        return this;
    }

    private void performInitialization() {
        if (!initialized) {
            executor.setListener(modelBuilder);
            initialize();
            initialized = true;
        }
    }
}
