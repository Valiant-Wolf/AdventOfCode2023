package net.valiantwolf.aoc23.day06;

import com.google.common.base.Splitter;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public class Day06A {

    public static void main(String... args) {

        var input = InputUtil.argFileAsLineStream(args).toList();

        var timeString = input.get(0);
        var distanceString = input.get(1);

        var times = Splitter.on(' ')
                            .omitEmptyStrings()
                            .trimResults()
                            .splitToStream(timeString.substring(timeString.indexOf(' ') + 1))
                            .mapToInt(Integer::parseInt)
                            .toArray();

        var distances = Splitter.on(' ')
                                .omitEmptyStrings()
                                .trimResults()
                                .splitToStream(distanceString.substring(distanceString.indexOf(' ') + 1))
                                .mapToInt(Integer::parseInt)
                                .toArray();

        var result = IntStream.range(0, times.length)
                              .map(i -> {
                                  var solution = solve(times[i], distances[i]);
                                  return (int) (Math.floor(solution[1]) - Math.floor(solution[0]));
                              })
                              .reduce(1, (p, q) -> p * q);

        System.out.println(result);
    }

    private static double[] solve(double t, double d) {
        var disc = Math.sqrt(Math.pow(t, 2) - (4 * d));
        return new double[]{ (t - disc) / 2, (t + disc) / 2 };
    }

}
