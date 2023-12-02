package io.github.itskillerluc.aoc.challenges;

import io.github.itskillerluc.aoc.Challenge;
import io.github.itskillerluc.aoc.utility.Pair;
import io.github.itskillerluc.aoc.utility.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Day2 extends Challenge {
    record Game(List<Integer> red, List<Integer> blue, List<Integer> green) {
    }

    @Override
    protected String part1() throws URISyntaxException, IOException {
        return String.valueOf(getInputList().stream().map(string -> {
            var game = string.split(":");
            var values = game[1].stripLeading().replace(';', ',').split(",");

            List<Integer> red = new ArrayList<>();
            List<Integer> blue = new ArrayList<>();
            List<Integer> green = new ArrayList<>();

            var colors = Arrays.stream(values).map(value -> {
                var split = value.stripLeading().split(" ");
                return new Pair<>(split[0], split[1]);
            }).toList();

            for (Pair<String, String> color : colors) {
                if (color.second().equals("red")) {
                    red.add(Integer.parseInt(color.first()));
                } else if (color.second().equals("blue")) {
                    blue.add(Integer.parseInt(color.first()));
                } else {
                    green.add(Integer.parseInt(color.first()));
                }
            }

            return Map.entry(game[0].replace("Game ", ""), new Game(red, blue, green));
        }).filter((Map.Entry<String, Day2.Game> entry) ->
                entry.getValue().red().stream().allMatch(red -> red <= 12) &&
                        entry.getValue().blue().stream().allMatch(blue -> blue <= 14) &&
                        entry.getValue().green().stream().allMatch(green -> green <= 13)
        ).mapToInt((Map.Entry<String, Day2.Game> entry) -> Integer.parseInt(entry.getKey())).sum());
    }

    @Override
    protected String part2() throws URISyntaxException, IOException {
        return String.valueOf(getInputList().stream().mapToInt(string -> {
            var game = string.split(":");
            var values = game[1].stripLeading().replace(';', ',').split(",");

            int redMax = 0;
            int greenMax = 0;
            int blueMax = 0;

            var colors = Arrays.stream(values).map(value -> {
                var split = value.stripLeading().split(" ");
                return new Pair<>(split[0], split[1]);
            }).toList();

            for (Pair<String, String> color : colors) {
                if (color.second().equals("red")) {
                    if (redMax < Integer.parseInt(color.first())) {
                        redMax = Integer.parseInt(color.first());
                    }
                } else if (color.second().equals("blue")) {
                    if (blueMax < Integer.parseInt(color.first())) {
                        blueMax = Integer.parseInt(color.first());
                    }
                } else {
                    if (greenMax < Integer.parseInt(color.first())) {
                        greenMax = Integer.parseInt(color.first());
                    }
                }
            }

            return redMax * greenMax * blueMax;
        }).sum());
    }

    @Override
    protected String day() {
        return "day_2";
    }
}
