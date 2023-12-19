package net.valiantwolf.aoc23.day08;

import net.valiantwolf.aoc23.util.InputUtil;
import net.valiantwolf.aoc23.util.MathUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day08B {

    private static final Pattern MAPPING_PATTERN = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");

    public static void main(String... args) {

        var directionString = InputUtil.argFileAsLineStream(args)
                                       .limit(1)
                                       .findFirst()
                                       .get();

        var directions = directionString.chars()
                                        .mapToObj(o -> o == 'L')
                                        .toList();

        var input = InputUtil.argFileAsLineStream(args)
                             .skip(2);

        Map<String, Pair> nodes = input.map(MAPPING_PATTERN::matcher)
                                       .filter(Matcher::find)
                                       .collect(Collectors.toMap(
                                               o -> o.group(1),
                                               o -> new Pair(o.group(2), o.group(3))
                                       ));


        var result = MathUtil.lowestCommonMultiple(
                nodes.keySet()
                     .stream()
                     .filter(o -> o.endsWith("A"))
                     .parallel()
                     .mapToInt(o -> findCycleLength(directions, nodes, o))
                     .toArray()
        );

        System.out.println(result * directions.size());

    }

    private static int findCycleLength(List<Boolean> directions, Map<String, Pair> nodes, String start) {
        int result = 0;
        String tortoise = start;
        String hare = start;

        do {
            tortoise = traverse(directions, nodes, tortoise);
            hare = traverse(directions, nodes, hare);
            hare = traverse(directions, nodes, hare);
            result++;
        } while (!tortoise.equals(hare));

        return result;
    }

    private static String traverse(List<Boolean> directions, Map<String, Pair> nodes, String start) {
        return directions.stream()
                         .reduce(start,
                                 (p, q) -> q
                                           ? nodes.get(p).left
                                           : nodes.get(p).right,
                                 (p, q) -> p);
    }

    private static record Pair(String left, String right) {
    }

}
