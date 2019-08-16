package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;

public class AnnotationWithPrivateValue implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnotationWithPrivateValue.class;
    }

    private String value() {
        return "some value";
    }
}
