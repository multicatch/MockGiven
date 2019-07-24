package xyz.multicatch.mockgiven.core.scenario.state;

import java.util.concurrent.atomic.AtomicReference;

public class CurrentScenarioState {
    private static final AtomicReference<Object> currentStep = new AtomicReference<>();

    public synchronized Object getCurrentStage() {
        return currentStep.get();
    }

    public synchronized void setCurrentStep(Object object) {
        currentStep.set(object);
    }
}
