package net.valiantwolf.aoc23.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.valiantwolf.aoc23.math.Interval.Relationship.AFTER;
import static net.valiantwolf.aoc23.math.Interval.Relationship.BEFORE;
import static net.valiantwolf.aoc23.math.Interval.Relationship.BEGINNING_OF;
import static net.valiantwolf.aoc23.math.Interval.Relationship.BEGINS_WITH;
import static net.valiantwolf.aoc23.math.Interval.Relationship.CONTAINED;
import static net.valiantwolf.aoc23.math.Interval.Relationship.ENDS_WITH;
import static net.valiantwolf.aoc23.math.Interval.Relationship.END_OF;
import static net.valiantwolf.aoc23.math.Interval.Relationship.MATCH;
import static net.valiantwolf.aoc23.math.Interval.Relationship.OVERLAP_ALL;
import static net.valiantwolf.aoc23.math.Interval.Relationship.OVERLAP_END;
import static net.valiantwolf.aoc23.math.Interval.Relationship.OVERLAP_START;

public class Interval {

    private final long start;
    private final long end;

    public Interval(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public final boolean overlaps(Interval that) {
        return this.start >= that.start
                && this.end <= that.end;
    }

    public Interval[] split(long split) {
        return new Interval[]{ new Interval(start, split), new Interval(split, end) };
    }

    public Interval translated(long translation) {
        return new Interval(start + translation, end + translation);
    }

    public boolean isEmpty() {
        return start == end;
    }

    public Relationship positionOf(Interval that) {
        if (this.start == that.start && this.end == that.end) {
            return MATCH;
        }

        if (that.end <= this.start) {
            return BEFORE;
        }

        if (that.start >= this.end) {
            return AFTER;
        }

        boolean before = that.start < this.start;
        boolean after = that.end > this.end;

        long startDiff = Long.signum(that.start - this.start);
        long endDiff = Long.signum(that.end - this.end);

        int diff = (int) (startDiff + endDiff);

        return switch (diff) {
            case -2 -> OVERLAP_START;
            case 2 -> OVERLAP_END;
            case 0 -> startDiff < endDiff
                       ? OVERLAP_ALL
                       : CONTAINED;
            case 1 -> startDiff == 0
                       ? BEGINS_WITH
                       : END_OF;
            case -1 -> startDiff == 0
                       ? BEGINNING_OF
                       : ENDS_WITH;
            default -> null;
        };
    }

    public static List<Interval> mergeContiguous(List<Interval> intervals) {
        if (intervals.isEmpty()) {
            return Collections.emptyList();
        }

        var working = new ArrayList<>(intervals);
        working.sort(Interval::startComparator);

        var result = new ArrayList<Interval>();

        var iter = working.listIterator();
        var hold = iter.next();

        while (iter.hasNext()) {
            var current = iter.next();

            if (hold.end < current.start) {
                result.add(hold);
                hold = current;
                continue;
            }

            hold = new Interval(hold.start, Long.max(hold.end, current.end));
        }

        result.add(hold);

        return result;
    }

    public static int startComparator(Interval p, Interval q) {
        return Long.compare(p.start, q.start);
    }

    public enum Relationship {
        /**
         * B is entirely before A
         */
        BEFORE,
        /**
         * B begins before A and ends within A
         */
        OVERLAP_START,
        /**
         * B begins and ends outside A
         */
        OVERLAP_ALL,
        /**
         * B begins within A and ends after A
         */
        OVERLAP_END,
        /**
         * B is entirely after A
         */
        AFTER,
        /**
         * B is entirely within A
         */
        CONTAINED,
        /**
         * B begins with A and ends after A
         */
        BEGINS_WITH,
        /**
         * B begins before A and ends with A
         */
        ENDS_WITH,
        /**
         * B begins with A and ends within A
         */
        BEGINNING_OF,
        /**
         * B begins within A and ends with A
         */
        END_OF,
        /**
         * B begins and ends with A
         */
        MATCH;
    }

}
