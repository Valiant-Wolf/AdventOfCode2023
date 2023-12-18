package net.valiantwolf.aoc23.day06;

import net.valiantwolf.aoc23.util.InputUtil;

public class Day06B {

    public static void main(String... args) {

        var input = InputUtil.argFileAsLineStream(args).toList();

        var timeString = input.get(0);
        var distanceString = input.get(1);

        timeString = timeString.substring(timeString.indexOf(' ') + 1);
        distanceString = distanceString.substring(distanceString.indexOf(' ') + 1);

        var time = Double.parseDouble(timeString.replaceAll(" ", ""));
        var distance = Double.parseDouble(distanceString.replaceAll(" ", ""));

        var solution = solve(time, distance);

        var result = (int) (Math.floor(solution[1]) - Math.floor(solution[0]));

        System.out.println(result);
    }

    private static double[] solve(double t, double d) {
        var disc = Math.sqrt(Math.pow(t, 2) - (4 * d));
        return new double[]{ (t - disc) / 2, (t + disc) / 2 };
    }

}
