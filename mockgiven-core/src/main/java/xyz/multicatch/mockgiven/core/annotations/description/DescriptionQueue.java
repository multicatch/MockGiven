package xyz.multicatch.mockgiven.core.annotations.description;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import com.tngtech.jgiven.report.model.Word;

public class DescriptionQueue {
    private static final Queue<DescriptionData> currentDescription = new ConcurrentLinkedQueue<>();

    public synchronized void add(DescriptionData description) {
        currentDescription.add(description);
    }

    public synchronized DescriptionData join() {
        if (currentDescription.isEmpty()) {
            return null;
        }

        List<DescriptionData> description = new ArrayList<>();

        for (DescriptionData buffer = currentDescription.poll(); buffer != null; buffer = currentDescription.poll()) {
            description.add(buffer);
        }

        String name = description.stream()
                                       .map(DescriptionData::getName)
                                       .collect(Collectors.joining(" "))
                                       .trim();

        List<Word> words = description.stream()
                                        .map(DescriptionData::getWords)
                                        .flatMap(List::stream)
                                        .collect(Collectors.toList());

        return new DescriptionData(name, words);
    }
}
