package xyz.multicatch.mockgiven.core.scenario.cases;

import java.util.List;
import com.tngtech.jgiven.annotation.CaseAs;
import lombok.Value;

@Value
public class CaseDescription {
    final CaseAs caseAs;
    final List values;
}
