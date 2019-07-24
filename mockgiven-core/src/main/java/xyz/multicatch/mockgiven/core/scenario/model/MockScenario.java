package xyz.multicatch.mockgiven.core.scenario.model;

import xyz.multicatch.mockgiven.core.scenario.MockScenarioBase;
import xyz.multicatch.mockgiven.core.stages.Action;
import xyz.multicatch.mockgiven.core.stages.Outcome;
import xyz.multicatch.mockgiven.core.stages.State;

public class MockScenario<STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> extends MockScenarioBase {

    private STATE givenStage;
    private ACTION whenStage;
    private OUTCOME thenStage;
    private final Class<STATE> givenClass;
    private final Class<ACTION> whenClass;
    private final Class<OUTCOME> thenClass;

    private MockScenario(Class<STATE> stageClass) {
        this.givenClass = stageClass;
        this.whenClass = null;
        this.thenClass = null;
    }

    public MockScenario(
            Class<STATE> givenClass,
            Class<ACTION> whenClass,
            Class<OUTCOME> thenClass
    ) {
        this.givenClass = givenClass;
        this.whenClass = whenClass;
        this.thenClass = thenClass;
    }

    public STATE getGivenStage() {
        return givenStage;
    }

    public ACTION getWhenStage() {
        return whenStage;
    }

    public OUTCOME getThenStage() {
        return thenStage;
    }

    public void addIntroWord(String word) {
        executor.addIntroWord(word);
    }

    public static <STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> MockScenario<STATE, ACTION, OUTCOME> create(
            Class<STATE> givenClass,
            Class<ACTION> whenClass,
            Class<OUTCOME> thenClass
    ) {
        return new MockScenario<>(givenClass, whenClass, thenClass);
    }

    @Override
    public MockScenario<STATE, ACTION, OUTCOME> startScenario(String description) {
        super.startScenario(description);
        return this;

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initialize() {
        super.initialize();
        givenStage = executor.addStage(givenClass);
        whenStage = executor.addStage(whenClass);
        thenStage = executor.addStage(thenClass);
    }

    public STATE given() {
        return given("Given");
    }

    public ACTION when() {
        return when("When");
    }

    public OUTCOME then() {
        return then("Then");
    }

    public STATE given(String translatedGiven) {
        addIntroWord(translatedGiven);
        return getGivenStage();
    }

    public ACTION when(String translatedWhen) {
        addIntroWord(translatedWhen);
        return getWhenStage();
    }

    public OUTCOME then(String translatedThen) {
        addIntroWord(translatedThen);
        return getThenStage();
    }

}
