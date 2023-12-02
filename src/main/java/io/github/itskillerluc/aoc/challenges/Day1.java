package io.github.itskillerluc.aoc.challenges;

import io.github.itskillerluc.aoc.Challenge;
import io.github.itskillerluc.aoc.utility.Pair;
import io.github.itskillerluc.aoc.utility.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Day1 extends Challenge {

    @Override
    protected String part1() throws URISyntaxException, IOException {
        return String.valueOf(getInputList().stream()
                .map(string -> {
                    var numbers = string.chars().filter(i -> i > '0' && i <= '9').toArray();
                    return String.valueOf((char)numbers[0]) + (char)numbers[numbers.length - 1];
                }).mapToInt(Integer::parseInt).sum());
    }

    @Override
    protected String part2() throws URISyntaxException, IOException {
        return String.valueOf(getInputList().stream()
                        .map(e -> e + e)
                        .map(string -> {
                            final AtomicReference<String> result = new AtomicReference<>(string);
                            List<Pair<Integer, String>> priority = new ArrayList<>();
                            for (int i = 0; i < Utils.WORD_NUMBERS.size(); i++) {
                                priority.add(new Pair<>(result.get().indexOf(Utils.WORD_NUMBERS.get(i)), Utils.WORD_NUMBERS.get(i)));
                            }
                            var firstEntry = priority.stream().filter(n -> n.first() >= 0).min(Comparator.comparingInt(Pair::first)).orElse(null);
                            if (firstEntry == null) return string;
                            result.set(result.get().replaceFirst(firstEntry.second(), String.valueOf(Utils.WORD_NUMBERS.indexOf(firstEntry.second()))));

                            return result.get();
                        })
                .map(string -> {
                    final AtomicReference<String> result = new AtomicReference<>(string);
                    List<Pair<Integer, String>> priority = new ArrayList<>();
                    for (int i = 0; i < Utils.WORD_NUMBERS.size(); i++) {
                        priority.add(new Pair<>(result.get().lastIndexOf(Utils.WORD_NUMBERS.get(i)), Utils.WORD_NUMBERS.get(i)));
                    }
                    var lastEntry = priority.stream().filter(n -> n.first() >= 0).max(Comparator.comparingInt(Pair::first)).orElse(null);
                    if (lastEntry == null) return string;
                    result.set(result.get().replace(lastEntry.second(), String.valueOf(Utils.WORD_NUMBERS.indexOf(lastEntry.second()))));

                    return result.get();
                })
                .map(string -> {
                    var numbers = string.chars().filter(i -> i >= '0' && i <= '9').toArray();
                    return numbers.length > 1 ? String.valueOf((char)numbers[0]) + (char)numbers[numbers.length - 1] : String.valueOf((char)numbers[0]);
                }).mapToInt(Integer::parseInt).sum());
    }

    @Override
    protected String day() {
        return "day_1";
    }
}
