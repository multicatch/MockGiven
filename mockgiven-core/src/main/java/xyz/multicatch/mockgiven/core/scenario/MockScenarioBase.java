package xyz.multicatch.mockgiven.core.scenario;

import java.lang.reflect.Method;
import java.util.List;
import com.tngtech.jgiven.impl.ScenarioBase;
import com.tngtech.jgiven.impl.ScenarioModelBuilder;
import com.tngtech.jgiven.report.model.NamedArgument;
import com.tngtech.jgiven.report.model.ReportModel;
import com.tngtech.jgiven.report.model.ScenarioCaseModel;
import com.tngtech.jgiven.report.model.ScenarioModel;
import xyz.multicatch.mockgiven.core.resources.TextResourceProvider;
import xyz.multicatch.mockgiven.core.scenario.creator.ByteBuddyStageClassCreator;
import xyz.multicatch.mockgiven.core.scenario.model.MockScenarioModelBuilder;
import xyz.multicatch.mockgiven.core.scenario.state.CurrentScenarioState;

public class MockScenarioBase extends ScenarioBase {
    protected final TextResourceProvider textResourceProvider;

    private final CurrentScenarioState currentScenarioState;
    private final ScenarioModelBuilder modelBuilder;
    private boolean initialized = false;

    public MockScenarioBase(TextResourceProvider textResourceProvider) {
        this.textResourceProvider = textResourceProvider;
        this.currentScenarioState = new CurrentScenarioState();
        this.modelBuilder = new MockScenarioModelBuilder(currentScenarioState, textResourceProvider);

        initScenarioExecutor();
    }

    public MockScenarioBase(TextResourceProvider textResourceProvider, CurrentScenarioState currentScenarioState, ScenarioModelBuilder scenarioModelBuilder) {
        this.textResourceProvider = textResourceProvider;
        this.currentScenarioState = currentScenarioState;
        this.modelBuilder = scenarioModelBuilder;
        initScenarioExecutor();
    }

    private void initScenarioExecutor() {
        MockScenarioExecutor scenarioExecutor = new MockScenarioExecutor();
        scenarioExecutor.setStageClassCreator(new ByteBuddyStageClassCreator());
        setExecutor(scenarioExecutor);
    }

    public void setModel(ReportModel reportModel) {
        assertNotInitialized();
        modelBuilder.setReportModel(reportModel);
    }

    public void setExecutor(MockScenarioExecutor executor) {
        super.setExecutor(executor);
    }

    public MockScenarioExecutor getExecutor() {
        return (MockScenarioExecutor) this.executor;
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
