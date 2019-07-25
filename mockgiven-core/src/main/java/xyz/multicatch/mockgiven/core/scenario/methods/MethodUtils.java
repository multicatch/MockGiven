package xyz.multicatch.mockgiven.core.scenario.methods;

import java.util.Optional;
import xyz.multicatch.mockgiven.core.stages.DescriptiveStage;

public class MethodUtils {

    public static Optional<String> extractPrefix(Object stage) {
        if (stage instanceof DescriptiveStage) {
            DescriptiveStage descriptiveStage = (DescriptiveStage) stage;
            String prefix = descriptiveStage.getCurrentPrefix();

            if (prefix != null) {
                return Optional.of(prefix);
            } else {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
