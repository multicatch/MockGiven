package xyz.multicatch.mockgiven.core.scenario;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import com.tngtech.jgiven.impl.ScenarioExecutor;
import com.tngtech.jgiven.impl.intercept.StageInterceptorInternal;
import com.tngtech.jgiven.impl.intercept.StepInterceptor;
import xyz.multicatch.mockgiven.core.scenario.creator.ByteBuddyStageClassCreator;

public class MockScenarioExecutor extends ScenarioExecutor {

    private final ByteBuddyStageClassCreator byteBuddyStageClassCreator = new ByteBuddyStageClassCreator();

    @SuppressWarnings("unchecked")
    public <T> T assertInterception(
            Class<T> type,
            Object constructorParam
    ) {
        try {
            Class<? extends T> interceptableAssertion = byteBuddyStageClassCreator.createStageClass(type);
            Constructor<?>[] constructors = interceptableAssertion.getDeclaredConstructors();
            T result = null;

            for (Constructor constructor : constructors) {
                if (constructor.getParameterCount() == 1) {
                    result = (T) constructor.newInstance(constructorParam);
                }
            }
            setStepInterceptor(result, methodInterceptor);
            stages.put(type, createStageState(result));
            return result;
        } catch (Error e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to create an instance of class " + type, e);
        }
    }

    protected StageState createStageState(Object instance) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = StageState.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return (StageState) constructor.newInstance(instance);
    }

    protected <T> void setStepInterceptor(
            T result,
            StepInterceptor stepInterceptor
    ) {
        ((StageInterceptorInternal) result).__jgiven_setStepInterceptor(stepInterceptor);
    }
}
