package net.valiantwolf.aoc23.day09;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class Day09B {

    public static void main(String... args) {

        var input = InputUtil.argFileAsLineStream(args);

        var result = input.parallel()
                          .map(o -> Splitter.on(" ")
                                            .splitToStream(o)
                                            .mapToLong(Long::parseLong)
                                            .toArray())
                          .mapToLong(Day09B::previous)
                          .sum();

        System.out.println(result);
    }

    private static long previous(long[] sequence) {

        if (Arrays.stream(sequence).allMatch(o -> o == 0)) {
            return 0;
        }

        var derivative = Streams.zip(Arrays.stream(sequence).boxed(),
                                     Arrays.stream(sequence).skip(1).boxed(),
                                     (p, q) -> q - p).mapToLong(Long::longValue)
                                .toArray();

        return sequence[0] - previous(derivative);
    }
}
