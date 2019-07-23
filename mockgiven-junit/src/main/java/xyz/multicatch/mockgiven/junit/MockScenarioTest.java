package xyz.multicatch.mockgiven.junit;

import org.junit.ClassRule;
import org.junit.Rule;
import com.tngtech.jgiven.impl.Scenario;
import com.tngtech.jgiven.junit.JGivenClassRule;
import com.tngtech.jgiven.junit.JGivenMethodRule;
import xyz.multicatch.mockgiven.core.stages.Outcome;
import xyz.multicatch.mockgiven.core.MockScenarioTestBase;
import xyz.multicatch.mockgiven.core.stages.State;
import xyz.multicatch.mockgiven.core.stages.Action;

@SuppressWarnings("unchecked")
public class MockScenarioTest<STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> extends MockScenarioTestBase<STATE, ACTION, OUTCOME> {
    @ClassRule
    public static final JGivenClassRule writerRule = new JGivenClassRule();

    @Rule
    public final JGivenMethodRule scenarioRule = new JGivenMethodRule( createScenario() );

    @Override
    public Scenario<STATE, ACTION, OUTCOME> getScenario() {
        return (Scenario<STATE, ACTION, OUTCOME>) scenarioRule.getScenario();
    }
}
