package net.valiantwolf.aoc23.day03;

import net.valiantwolf.aoc23.collections.QuadTree;
import net.valiantwolf.aoc23.math.Rect;
import net.valiantwolf.aoc23.math.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static net.valiantwolf.aoc23.util.InputUtil.argFileAsLineStream;

public class Day03A {

    private static final Pattern PATTERN = Pattern.compile("(\\d+)|([^\\d^.])");

    public static void main(String... args) {

        var input = argFileAsLineStream(args);

        QuadTree<String> parts = new QuadTree<>(4, 32);
        List<PartNumber> partNumbers = new LinkedList<>();

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
                           Rect rect = new Rect(new Vector(start - 1, currentRow - 1), new Vector(end + 1, currentRow + 2));
                           partNumbers.add(new PartNumber(rect, Integer.parseInt(match.group())));
                       } else {
                           parts.put(new Vector(start, currentRow), match.group());
                       }
                   });

            row++;
        }

        var result = partNumbers.stream()
                                .filter(o -> parts.search(o.rect).findFirst().isPresent())
                                .mapToInt(PartNumber::number)
                                .sum();

        System.out.println(result);
    }

    private static record PartNumber(Rect rect, int number) {

    }

}
