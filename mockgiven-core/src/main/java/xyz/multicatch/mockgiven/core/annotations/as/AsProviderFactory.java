package xyz.multicatch.mockgiven.core.annotations.as;

import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.AsProvider;
import com.tngtech.jgiven.impl.params.DefaultAsProvider;
import com.tngtech.jgiven.impl.util.ReflectionUtil;

public class AsProviderFactory {
    public static AsProvider create(As as) {
        return as != null
                ? ReflectionUtil.newInstance(as.provider())
                : new DefaultAsProvider();
    }
}
