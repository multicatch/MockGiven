package xyz.multicatch.mockgiven.core.scenario.methods;

import static com.tngtech.jgiven.impl.ByteBuddyStageClassCreator.INTERCEPTOR_FIELD_NAME;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import com.tngtech.jgiven.impl.ByteBuddyStageClassCreator;
import com.tngtech.jgiven.impl.intercept.ByteBuddyMethodInterceptor;
import net.bytebuddy.implementation.bind.annotation.*;
import xyz.multicatch.mockgiven.core.scenario.state.CurrentScenarioState;

public class MockMethodInterceptor extends ByteBuddyMethodInterceptor {
    private final CurrentScenarioState currentScenarioState = new CurrentScenarioState();

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 3)
    public Object interceptSuper(
            @SuperCall final Callable<?> zuper,
            @This final Object receiver,
            @Origin Method method,
            @AllArguments final Object[] parameters,
            @FieldProxy(INTERCEPTOR_FIELD_NAME) ByteBuddyStageClassCreator.StepInterceptorGetterSetter stepInterceptorGetter
    ) throws Throwable {
        currentScenarioState.setCurrentStep(receiver);
        return super.interceptSuper(zuper, receiver, method, parameters, stepInterceptorGetter);
    }

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 2)
    public Object interceptDefault(
            @DefaultCall final Callable<?> zuper,
            @This final Object receiver,
            @Origin Method method,
            @AllArguments final Object[] parameters,
            @FieldProxy(INTERCEPTOR_FIELD_NAME) ByteBuddyStageClassCreator.StepInterceptorGetterSetter stepInterceptorGetter
    )
            throws Throwable {

        currentScenarioState.setCurrentStep(receiver);
        return super.interceptDefault(zuper, receiver, method, parameters, stepInterceptorGetter);
    }

    @RuntimeType
    public Object intercept(
            @This final Object receiver,
            @Origin final Method method,
            @AllArguments final Object[] parameters,
            @FieldProxy(INTERCEPTOR_FIELD_NAME) ByteBuddyStageClassCreator.StepInterceptorGetterSetter stepInterceptorGetter
    )
            throws Throwable {

        currentScenarioState.setCurrentStep(receiver);
        return super.intercept(receiver, method, parameters, stepInterceptorGetter);
    }

}
