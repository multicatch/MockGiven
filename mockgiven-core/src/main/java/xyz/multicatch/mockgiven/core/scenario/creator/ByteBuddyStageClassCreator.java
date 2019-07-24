package xyz.multicatch.mockgiven.core.scenario.creator;

import static com.tngtech.jgiven.impl.ByteBuddyStageClassCreator.INTERCEPTOR_FIELD_NAME;
import static com.tngtech.jgiven.impl.ByteBuddyStageClassCreator.SETTER_NAME;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;
import com.tngtech.jgiven.impl.StageClassCreator;
import com.tngtech.jgiven.impl.intercept.StageInterceptorInternal;
import com.tngtech.jgiven.impl.intercept.StepInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.FieldProxy;
import net.bytebuddy.matcher.ElementMatchers;
import xyz.multicatch.mockgiven.core.scenario.methods.MockMethodInterceptor;

public class ByteBuddyStageClassCreator implements StageClassCreator {

    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> createStageClass(Class<T> stageClass) {
        return new ByteBuddy()
                .subclass(stageClass, ConstructorStrategy.Default.IMITATE_SUPER_CLASS_OPENING)
                .implement(StageInterceptorInternal.class)
                .defineField(INTERCEPTOR_FIELD_NAME, StepInterceptor.class)
                .method(named(SETTER_NAME))
                .intercept(
                        MethodDelegation.withDefaultConfiguration()
                                        .withBinders(FieldProxy.Binder.install(
                                                com.tngtech.jgiven.impl.ByteBuddyStageClassCreator.StepInterceptorGetterSetter.class))
                                        .to(new com.tngtech.jgiven.impl.ByteBuddyStageClassCreator.StepInterceptorSetter()))
                .method(not(named(SETTER_NAME)
                        .or(ElementMatchers.isDeclaredBy(Object.class))))
                .intercept(
                        MethodDelegation.withDefaultConfiguration()
                                        .withBinders(FieldProxy.Binder.install(
                                                com.tngtech.jgiven.impl.ByteBuddyStageClassCreator.StepInterceptorGetterSetter.class))
                                        .to(new MockMethodInterceptor()))
                .make()
                .load(getClassLoader(stageClass),
                        getClassLoadingStrategy(stageClass))
                .getLoaded();
    }

    protected ClassLoadingStrategy getClassLoadingStrategy(Class<?> stageClass) {
        return getClassLoader(stageClass) == null
                ? ClassLoadingStrategy.Default.WRAPPER
                : ClassLoadingStrategy.Default.INJECTION;
    }

    protected ClassLoader getClassLoader(Class<?> stageClass) {
        return stageClass.getClassLoader();
    }

}
