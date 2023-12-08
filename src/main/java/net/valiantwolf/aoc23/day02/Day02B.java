package net.valiantwolf.aoc23.day02;

import com.google.common.base.Splitter;

import java.util.List;

import static java.lang.Integer.max;
import static net.valiantwolf.aoc23.util.InputUtil.argFileAsLineStream;

public class Day02B {

    public static void main(String... args) {

        var input = argFileAsLineStream(args);

        int result = input.map(Game::parse)
                          .map(o -> o.rounds.stream()
                                            .reduce(new Round(0, 0, 0),
                                                    (p, q) -> new Round(max(p.red, q.red),
                                                                        max(p.green, q.green),
                                                                        max(p.blue, q.blue))
                                            ))
                          .mapToInt(o -> o.red * o.green * o.blue)
                          .sum();

        System.out.println(result);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static record Game(int number, List<Round> rounds) {

        static Game parse(String input) {
            var parts = input.split(": ");

            int number = Integer.parseInt(parts[0].substring(5));

            List<Round> rounds = Splitter.on("; ")
                                         .splitToStream(parts[1])
                                         .map(Round::parse)
                                         .toList();

            return new Game(number, rounds);
        }

    }

    private static record Round(int red, int green, int blue) {

        static Round parse(String input) {

            int red = 0, green = 0, blue = 0;

            for (var colour : Splitter.on(", ").split(input)) {
                var parts = colour.split(" ");
                int number = Integer.parseInt(parts[0]);
                switch (parts[1]) {
                    case "red" -> red = number;
                    case "green" -> green = number;
                    case "blue" -> blue = number;
                }
            }

            return new Round(red, green, blue);
        }

    }
}
