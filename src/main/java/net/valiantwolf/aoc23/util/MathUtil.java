package net.valiantwolf.aoc23.util;

import com.google.common.math.IntMath;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public abstract class MathUtil {
    public static int clamp(int value, int min, int max) {
        return value < min
               ? min
               : Math.min(value, max);
    }

    public static Map<Integer, Integer> primeFactors(int number) {
        Map<Integer, Integer> result = new TreeMap<>();

        while (number % 2 == 0) {
            number /= 2;
            result.compute(2, (k, v) -> v == null ? 1 : v + 1);
        }

        var sqrt = Math.sqrt(number);

        for (int i = 3; i <= sqrt; i += 2) {

            while (number % i == 0) {
                number /= i;
                result.compute(i, (k, v) -> v == null ? 1 : v + 1);
            }

        }

        if (number > 2) {
            result.put(number, 1);
        }

        return result;
    }

    public static long lowestCommonMultiple(int... numbers) {
        return Arrays.stream(numbers)
                     .parallel()
                     .mapToObj(MathUtil::primeFactors)
                     .reduce(new TreeMap<>(), (p, q) -> {
                         q.forEach((k, v) -> p.merge(k, v, Integer::max));
                         return p;
                     })
                     .entrySet()
                     .stream()
                     .mapToLong(o -> IntMath.pow(o.getKey(), o.getValue()))
                     .reduce(1L, (p, q) -> p * q);
    }
}
