package net.valiantwolf.aoc23.day03;

import net.valiantwolf.aoc23.collections.QuadTree;
import net.valiantwolf.aoc23.math.Rect;
import net.valiantwolf.aoc23.math.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.valiantwolf.aoc23.util.InputUtil.argFileAsLineStream;

public class Day03B {

    private static final Pattern PATTERN = Pattern.compile("(\\d+)|([^\\d^.])");

    public static void main(String... args) {

        var input = argFileAsLineStream(args);

        QuadTree<String> parts = new QuadTree<>(4, 32);
        List<PartSearch> partSearches = new LinkedList<>();

        var iter = input.iterator();
        int row = 0;

        while (iter.hasNext()) {
            String line = iter.next();
            final int currentRow = row;

            PATTERN.matcher(line)
                   .results()
                   .forEach(match -> {
                       int start = match.start();
                       int end = match.end();

                       if (match.group(1) != null) {
                           Rect rect = new Rect(new Vector(start - 1, currentRow - 1),
                                                new Vector(end + 1, currentRow + 2));
                           partSearches.add(new PartSearch(rect, Integer.parseInt(match.group())));
                       } else {
                           parts.put(new Vector(start, currentRow), match.group());
                       }
                   });

            row++;
        }

        var result = partSearches.stream()
                                 .flatMap(o -> parts.search(o.rect)
                                                    .findFirst()
                                                    .map(p -> new PartNumber(p.key(), o.number, p.value()))
                                                    .stream())
                                 .filter(o -> o.part.equals("*"))
                                 .collect(Collectors.groupingBy(PartNumber::position))
                                 .values()
                                 .stream()
                                 .filter(o -> o.size() == 2)
                                 .mapToInt(o -> o.get(0).number * o.get(1).number)
                                 .sum();

        System.out.println(result);
    }

    private static record PartSearch(Rect rect, int number) {

    }

    private static record PartNumber(Vector position, int number, String part) {

    }

}
