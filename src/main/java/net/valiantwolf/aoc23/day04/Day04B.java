package net.valiantwolf.aoc23.day04;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04B {

    public static void main(String... args) {

        var input = InputUtil.argFileAsLineStream(args);

        LinkedList<Integer> copyQueue = new LinkedList<>();

        var result = input.map(ScratchCard::new)
                          .map(o -> Sets.intersection(o.getWinSet(), o.getHaveSet()))
                          .mapToInt(Set::size)
                          .map(o -> {
                              int copies = Optional.ofNullable(copyQueue.poll()).orElse(0) + 1;

                              for (int i = 0; i < o; i++) {
                                  if (i < copyQueue.size()) {
                                      copyQueue.set(i, copyQueue.get(i) + copies);
                                  } else {
                                      copyQueue.add(copies);
                                  }
                              }

                              return copies;
                          })
                          .sum();

        System.out.println(result);
    }

    private static class ScratchCard {

        private static final Pattern PATTERN = Pattern.compile("Card +\\d+:([^|]+)\\|(.*)");

        private final Set<Integer> winSet;
        private final Set<Integer> haveSet;

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
