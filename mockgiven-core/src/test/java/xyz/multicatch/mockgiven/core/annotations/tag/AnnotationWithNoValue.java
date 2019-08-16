package xyz.multicatch.mockgiven.core.annotations.tag;

import java.lang.annotation.Annotation;

public class AnnotationWithNoValue implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() {
        return AnnotationWithNoValue.class;
    }
}
