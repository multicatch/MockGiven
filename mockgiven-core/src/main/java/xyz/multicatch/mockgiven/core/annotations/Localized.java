package xyz.multicatch.mockgiven.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import xyz.multicatch.mockgiven.core.resources.TextResource;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Localized {
    TextResource value();
}
