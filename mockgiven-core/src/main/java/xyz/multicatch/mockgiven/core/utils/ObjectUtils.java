package xyz.multicatch.mockgiven.core.utils;

import java.util.List;
import com.google.common.collect.Lists;

public class ObjectUtils {

    private ObjectUtils() {}

    public static List<String> toList(Object[] value) {
        List<String> values = Lists.newArrayList();
        for (Object v : value) {
            values.add(String.valueOf(v));
        }
        return values;
    }
}
