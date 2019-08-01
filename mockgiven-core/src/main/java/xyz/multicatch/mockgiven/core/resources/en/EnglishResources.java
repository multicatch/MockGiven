package xyz.multicatch.mockgiven.core.resources.en;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import xyz.multicatch.mockgiven.core.resources.TextResource;
import xyz.multicatch.mockgiven.core.resources.TextResourceProvider;

public class EnglishResources implements TextResourceProvider {

    private static final Map<TextResource, String> RESOURCES = ImmutableMap.<TextResource, String>builder()
                                                                           .put(TextResource.GIVEN, "Given")
                                                                           .put(TextResource.WHEN, "When")
                                                                           .put(TextResource.THEN, "Then")
                                                                           .put(TextResource.AND, "And")
                                                                           .put(TextResource.BUT, "But")
                                                                           .put(TextResource.WITH, "With")
                                                                           .build();

    @Override
    public String get(TextResource resource) {
        return RESOURCES.get(resource);
    }
}
