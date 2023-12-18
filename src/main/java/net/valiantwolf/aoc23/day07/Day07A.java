package net.valiantwolf.aoc23.day07;

import net.valiantwolf.aoc23.util.InputUtil;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day07A {

    public static void main(String... args) {

        var input = InputUtil.argFileAsLineStream(args);

        AtomicInteger rank = new AtomicInteger();

        var result = input.map(Hand::new)
                          .sorted()
                          .peek(o -> System.out.println(Arrays.stream(o.getCards())
                                                              .mapToObj(String::valueOf)
                                                              .collect(Collectors.joining(" "))))
                          .mapToLong(Hand::getBid)
                          .map(o -> o * rank.incrementAndGet())
                          .sum();

        System.out.println(result);
    }

}
