package net.valiantwolf.aoc23.day09;

import com.google.common.base.Splitter;
import com.google.common.collect.Streams;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class Day09A {

    public static void main(String... args) {

        var input = InputUtil.argFileAsLineStream(args);

        var result = input.parallel()
                          .map(o -> Splitter.on(" ")
                                            .splitToStream(o)
                                            .mapToLong(Long::parseLong)
                                            .toArray())
                          .mapToLong(Day09A::next)
                          .sum();

        System.out.println(result);
    }

    private static long next(long[] sequence) {

        if (Arrays.stream(sequence).allMatch(o -> o == 0)) {
            return 0;
        }

        var derivative = Streams.zip(Arrays.stream(sequence).boxed(),
                                     Arrays.stream(sequence).skip(1).boxed(),
                                     (p, q) -> q - p).mapToLong(Long::longValue)
                                .toArray();

        return sequence[sequence.length - 1] + next(derivative);
    }
}
