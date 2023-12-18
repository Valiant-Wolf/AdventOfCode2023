package net.valiantwolf.aoc23.day05;

import com.google.common.base.Splitter;
import net.valiantwolf.aoc23.math.Interval;
import net.valiantwolf.aoc23.math.ValueInterval;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class Day05B {

    public static void main(String... args) {

        var lineIter = InputUtil.argFileAsLineStream(args).iterator();

        // grab the first line and convert into bare intervals
        String seedString = lineIter.next();
        seedString = seedString.substring(seedString.indexOf(' ') + 1);

        long[] seedArray = Splitter.on(' ')
                               .splitToStream(seedString)
                               .mapToLong(Long::parseLong)
                               .toArray();

        List<Interval> seeds = new ArrayList<>(seedArray.length / 2);

        for (int i = 0; i < seedArray.length; i += 2) {
            seeds.add(new Interval(seedArray[i], seedArray[i] + seedArray[i + 1]));
        }

        // grab each section and convert into a list of valued intervals
        // the value of each interval is the translation it applies to an input value
        List<List<ValueInterval<Long>>> mappings = new ArrayList<>();
        List<ValueInterval<Long>> workingList = new ArrayList<>();

        while (lineIter.hasNext()) {
            String line = lineIter.next();

            if (line.isBlank()) {
                continue;
            }

            if (line.charAt(0) < '0' || line.charAt(0) > '9') {
                mappings.add(workingList);
                workingList = new ArrayList<>();
                continue;
            }

            String[] parts = line.split(" ");

            long source = Long.parseLong(parts[1]);
            long destination = Long.parseLong(parts[0]);
            long width = Long.parseLong(parts[2]);

            workingList.add(new ValueInterval<>(source, source + width, destination - source));
        }

        mappings.add(workingList);

        // apply each mapping sequentially to the input intervals
        for (var mapping : mappings) {

            // the top iterator tracks over the input intervals
            // contiguous input intervals are merged (only care about the continuum, not individual intervals)
            seeds = Interval.mergeContiguous(seeds);
            var topIter = seeds.listIterator();

            // the top iterator tracks the mapping intervals
            mapping.sort(Interval::startComparator);
            var bottomIter = mapping.listIterator();

            var top = topIter.hasNext() ? topIter.next() : null;
            var bottom = bottomIter.hasNext() ? bottomIter.next() : null;

            // perform a continuous left-join and translate any joined sections by the mapping
            while (top != null && bottom != null) {

                switch (top.positionOf(bottom)) {
                    case BEFORE -> {
                        bottom = bottomIter.hasNext() ? bottomIter.next() : null;
                        continue;
                    }
                    case OVERLAP_START, BEGINNING_OF -> {
                        topIter.remove();
                        var split = top.split(bottom.getEnd());
                        topIter.add(split[0].translated(bottom.getValue()));
                        topIter.add(split[1]);

                        bottom = bottomIter.hasNext() ? bottomIter.next() : null;
                        topIter.previous();
                    }
                    case CONTAINED -> {
                        topIter.remove();
                        var firstSplit = top.split(bottom.getStart());
                        topIter.add(firstSplit[0]);
                        var secondSplit = firstSplit[1].split(bottom.getEnd());
                        topIter.add(secondSplit[0].translated(bottom.getValue()));
                        topIter.add(secondSplit[1]);

                        bottom = bottomIter.hasNext() ? bottomIter.next() : null;
                        topIter.previous();
                    }
                    case MATCH, ENDS_WITH -> {
                        topIter.remove();
                        topIter.add(top.translated(bottom.getValue()));

                        bottom = bottomIter.hasNext() ? bottomIter.next() : null;
                    }
                    case OVERLAP_END, END_OF -> {
                        topIter.remove();
                        var split = top.split(bottom.getStart());
                        topIter.add(split[0]);
                        topIter.add(split[1].translated(bottom.getValue()));
                    }
                    case OVERLAP_ALL, BEGINS_WITH -> {
                        topIter.remove();
                        topIter.add(top.translated(bottom.getValue()));
                    }
                    case AFTER -> {
                        //noop
                    }
                }

                top = topIter.hasNext() ? topIter.next() : null;
            }
        }

        seeds.sort(Interval::startComparator);

        var result = seeds.get(0).getStart();

        System.out.println(result);

    }

}
