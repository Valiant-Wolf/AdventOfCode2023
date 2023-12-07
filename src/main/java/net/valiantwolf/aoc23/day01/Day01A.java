package net.valiantwolf.aoc23.day01;

import java.util.stream.Stream;

import static net.valiantwolf.aoc23.util.InputUtil.argFileAsLineStream;

public class Day01A {

    public static void main(String... args) {

        Stream<String> input = argFileAsLineStream(args);

        int result = input.mapToInt(l -> {
                              int[] ints = l.chars()
                                            .map(c -> c - '0')
                                            .filter(c -> 0 <= c && c <= 9)
                                            .toArray();

                              return (10 * ints[0]) + ints[ints.length - 1];
                          })
                          .reduce(0, Integer::sum);

        System.out.println(result);

    }

}
