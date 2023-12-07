package net.valiantwolf.aoc23.day01;

import com.google.common.collect.ImmutableMap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.valiantwolf.aoc23.util.InputUtil.argFileAsLineStream;

public class Day01B {

    private static final Map<String, Integer> TOKEN_MAP =
            ImmutableMap.<String, Integer>builder()
                        .put("1", 1)
                        .put("2", 2)
                        .put("3", 3)
                        .put("4", 4)
                        .put("5", 5)
                        .put("6", 6)
                        .put("7", 7)
                        .put("8", 8)
                        .put("9", 9)
                        .put("0", 0)
                        .put("one", 1)
                        .put("two", 2)
                        .put("three", 3)
                        .put("four", 4)
                        .put("five", 5)
                        .put("six", 6)
                        .put("seven", 7)
                        .put("eight", 8)
                        .put("nine", 9)
                        .put("zero", 0)
                        .build();


    public static void main(String... args) {

        Stream<String> input = argFileAsLineStream(args);

        int result = input.map(Day01B::findInts)
                          .map(IntStream::toArray)
                          .mapToInt(Day01B::filletToInt)
                          .reduce(0, Integer::sum);

        System.out.println(result);

    }

    private static int filletToInt(int[] ints) {
        return (10 * ints[0]) + ints[ints.length - 1];
    }

    private static IntStream findInts(String input) {

        Map<Integer, Integer> occurrenceMap = new HashMap<>();

        TOKEN_MAP.forEach((key, value) -> {
            occurrenceMap.put(input.indexOf(key), value);
            occurrenceMap.put(input.lastIndexOf(key), value);
        });

        return occurrenceMap.entrySet()
                            .stream()
                            .filter(o -> o.getKey() >= 0)
                            .sorted(Comparator.comparing(Map.Entry::getKey))
                            .mapToInt(Map.Entry::getValue);
    }
}
