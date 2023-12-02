package io.github.itskillerluc.aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

public abstract class Challenge {
    public void run() {
        try {
            System.out.println(day());
            System.out.println();
            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
            System.out.println("======================================");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String part1() throws URISyntaxException, IOException;

    protected abstract String part2() throws URISyntaxException, IOException;

    protected abstract String day();

    protected String getInputString() throws URISyntaxException, IOException {
        return Files.readString(Main.getInput(day()));
    }

    protected List<String> getInputList() throws URISyntaxException, IOException {
        return Files.readAllLines(Main.getInput(day()));
    }
}
