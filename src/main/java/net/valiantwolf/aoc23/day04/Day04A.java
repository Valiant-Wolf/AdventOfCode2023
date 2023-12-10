package net.valiantwolf.aoc23.day04;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04A {

    public static void main(String... args) {

        var input = InputUtil.argFileAsLineStream(args);

        var result = input.map(ScratchCard::new)
                          .map(o -> Sets.intersection(o.getWinSet(), o.getHaveSet()))
                          .mapToInt(Set::size)
                          .filter(o -> o > 0)
                          .map(o -> IntMath.pow(2, o - 1))
                          .sum();

        System.out.println(result);
    }

    private static class ScratchCard {

        private static final Pattern PATTERN = Pattern.compile("Card +\\d+:([^|]+)\\|(.*)");

        private Set<Integer> winSet;
        private Set<Integer> haveSet = new TreeSet<>();

        @SuppressWarnings("UnstableApiUsage")
        private ScratchCard(String input) {
            var matcher = PATTERN.matcher(input);

            matcher.matches();
            winSet = Splitter.on(" ")
                             .omitEmptyStrings()
                             .splitToStream(matcher.group(1))
                             .map(Integer::parseInt)
                             .collect(Collectors.toCollection(TreeSet::new));

            haveSet = Splitter.on(" ")
                              .omitEmptyStrings()
                              .splitToStream(matcher.group(2))
                              .map(Integer::parseInt)
                              .collect(Collectors.toCollection(TreeSet::new));
        }

        public Set<Integer> getWinSet() {
            return winSet;
        }

        public Set<Integer> getHaveSet() {
            return haveSet;
        }
    }

}
