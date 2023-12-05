package io.github.itskillerluc.aoc;

import io.github.itskillerluc.aoc.challenges.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        init().forEach(Challenge::run);
    }

    public static Path getInput(String path) throws URISyntaxException {
        return Path.of(ClassLoader.getSystemResource(path).toURI());
    }

    private static List<Challenge> init() {
        List<Challenge> challenges = new ArrayList<>();
        challenges.add(new Day1());
        challenges.add(new Day2());
        challenges.add(new Day3());
        challenges.add(new Day4());
        challenges.add(new Day5());
        return challenges;
    }
}