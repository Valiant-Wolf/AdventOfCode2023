package net.valiantwolf.aoc23.day08;

import com.google.common.collect.Iterables;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day08A {

    private static final Pattern MAPPING_PATTERN = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");

    public static void main(String... args) {

        var directions = InputUtil.argFileAsLineStream(args)
                                  .limit(1)
                                  .findFirst()
                                  .get();

        var directionIter = Iterables.cycle(directions.chars()
                                                      .mapToObj(o -> o == 'L')
                                                      .collect(Collectors.toList()))
                                     .iterator();

        var input = InputUtil.argFileAsLineStream(args)
                             .skip(2);

        Map<String, Pair> nodes = input.map(MAPPING_PATTERN::matcher)
                                       .filter(Matcher::find)
                                       .collect(Collectors.toMap(
                                               o -> o.group(1),
                                               o -> new Pair(o.group(2), o.group(3))
                                       ));

        int result = 0;

        var current = "AAA";

        while (!current.equals("ZZZ")) {
            var node = nodes.get(current);
            current = directionIter.next()
                      ? node.left()
                      : node.right();

            result++;
        }

        System.out.println(result);

    }

    private static record Pair(String left, String right) {
    }

}
