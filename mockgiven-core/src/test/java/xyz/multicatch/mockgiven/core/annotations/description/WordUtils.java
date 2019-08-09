package xyz.multicatch.mockgiven.core.annotations.description;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.tngtech.jgiven.report.model.Word;

public class WordUtils {

    public static List<Word> wordsOf(String input) {
        return Stream.of(input.split(" "))
                     .map(Word::new)
                     .collect(Collectors.toList());
    }

}
