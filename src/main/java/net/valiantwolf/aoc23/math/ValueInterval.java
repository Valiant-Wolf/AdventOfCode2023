package net.valiantwolf.aoc23.math;

public class ValueInterval<T> extends Interval {

    private final T value;

    public ValueInterval(long start, long end, T value) {
        super(start, end);
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
