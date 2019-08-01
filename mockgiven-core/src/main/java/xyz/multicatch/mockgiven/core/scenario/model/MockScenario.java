package xyz.multicatch.mockgiven.core.scenario.model;

import xyz.multicatch.mockgiven.core.resources.TextResource;
import xyz.multicatch.mockgiven.core.resources.TextResourceProvider;
import xyz.multicatch.mockgiven.core.resources.en.EnglishResources;
import xyz.multicatch.mockgiven.core.scenario.MockScenarioBase;
import xyz.multicatch.mockgiven.core.stages.Action;
import xyz.multicatch.mockgiven.core.stages.AssertionStage;
import xyz.multicatch.mockgiven.core.stages.Outcome;
import xyz.multicatch.mockgiven.core.stages.State;

public class MockScenario<STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> extends MockScenarioBase {

    private STATE givenStage;
    private ACTION whenStage;
    private OUTCOME thenStage;
    private final Class<STATE> givenClass;
    private final Class<ACTION> whenClass;
    private final Class<OUTCOME> thenClass;

    private MockScenario(
            TextResourceProvider textResourceProvider,
            Class<STATE> stageClass
    ) {
        super(textResourceProvider);
        this.givenClass = stageClass;
        this.whenClass = null;
        this.thenClass = null;
    }

    public MockScenario(
            TextResourceProvider textResourceProvider,
            Class<STATE> givenClass,
            Class<ACTION> whenClass,
            Class<OUTCOME> thenClass
    ) {
        super(textResourceProvider);
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

    public void addIntroWord(TextResource resource) {
        executor.addIntroWord(textResourceProvider.get(resource));
    }

    public static <STATE extends State<?>, ACTION extends Action<?>, OUTCOME extends Outcome<?>> MockScenario<STATE, ACTION, OUTCOME> create(
            Class<STATE> givenClass,
            Class<ACTION> whenClass,
            Class<OUTCOME> thenClass
    ) {
        return new MockScenario<>(new EnglishResources(), givenClass, whenClass, thenClass);
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
        AssertionStage assertionStage = executor.addStage(AssertionStage.class);
        assertionStage.setExecutor(this.getExecutor());
    }

    public STATE given() {
        addIntroWord(TextResource.GIVEN);
        return getGivenStage();
    }

    public ACTION when() {
        addIntroWord(TextResource.WHEN);
        return getWhenStage();
    }

    public OUTCOME then() {
        addIntroWord(TextResource.THEN);
        return getThenStage();
    }
}
