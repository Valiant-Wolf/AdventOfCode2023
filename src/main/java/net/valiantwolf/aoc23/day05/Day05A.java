package net.valiantwolf.aoc23.day05;

import com.google.common.base.Splitter;
import net.valiantwolf.aoc23.util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.LongStream;

@SuppressWarnings("UnstableApiUsage")
public class Day05A {

    public static void main(String... args) {

        var lineIter = InputUtil.argFileAsLineStream(args).iterator();

        LongStream seeds = Splitter.on(" ")
                                   .splitToStream(lineIter.next())
                                   .skip(1)
                                   .mapToLong(Long::parseLong);

        List<TreeMap<Long, Mapping>> maps = new ArrayList<>();
        TreeMap<Long, Mapping> workingQueue = new TreeMap<>();

        while (lineIter.hasNext()) {
            String line = lineIter.next();

            if (line.isBlank()) {
                continue;
            }

            if (line.charAt(0) < '0' || line.charAt(0) > '9') {
                maps.add(workingQueue);
                workingQueue = new TreeMap<>();
                continue;
            }

            String[] parts = line.split(" ");

            long source = Long.parseLong(parts[1]);
            long destination = Long.parseLong(parts[0]);
            long width = Long.parseLong(parts[2]);

            workingQueue.put(source, new Mapping(destination - source, width));
        }

        maps.add(workingQueue);

        long result = seeds.map(o -> maps.stream()
                                         .reduce(o,
                                                 (p, q) -> p + Optional.ofNullable(q.floorEntry(p))
                                                                       .filter(r -> p < r.getKey() + r.getValue().width)
                                                                       .orElse(Map.entry(0L, new Mapping(0, 0)))
                                                                       .getValue().offset,
                                                 (p, q) -> p)
                           )
                           .min().orElse(-1);

        System.out.println(result);

    }

    private record Mapping(long offset, long width) {

    }

}
