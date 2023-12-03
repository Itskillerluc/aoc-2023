package io.github.itskillerluc.aoc.challenges;

import io.github.itskillerluc.aoc.Challenge;
import io.github.itskillerluc.aoc.utility.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Day3 extends Challenge {

    @Override
    protected String part1() throws URISyntaxException, IOException {
        char[][] input = getInputList().stream().map(String::toCharArray).toArray(char[][]::new);
        int output = 0;
        for (int y = 0; y < input.length; y++) {
            int skips = 0;
            for (int x = 0; x < input[x].length - 1; x++) {
                if (skips != 0) {
                    skips--;
                    continue;
                }
                if (input[y][x] >= '0' && input[y][x] <= '9') {
                    int numberLength = 0;
                    int index = 0;

                    try {
                        do {
                            numberLength++;
                            index++;
                        } while (input[y][x + index] >= '0' && input[y][x + index] <= '9');
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                    skips = numberLength;

                    boolean symbolAdjacent = false;

                    for (int j = Math.max(0, y - 1); j <= Math.min(139, y + 1); j++) {
                        for (int i = Math.max(0, x - 1); i < Math.min(139, x + numberLength + 1); i++) {
                            if ((input[j][i] < '0' || input[j][i] > '9') && input[j][i] != '.') {
                                symbolAdjacent = true;
                                break;
                            }
                        }
                    }

                    StringBuilder number = new StringBuilder();

                    for (int i = x; i < x + numberLength; i++) {
                        number.append(input[y][i]);
                    }

                    if (symbolAdjacent) {
                        output += Integer.parseInt(number.toString());
                    }
                }
            }
        }
        return String.valueOf(output);
    }

    @Override
    protected String part2() throws URISyntaxException, IOException {
        char[][] input = getInputList().stream().map(String::toCharArray).toArray(char[][]::new);
        int output = 0;
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[x].length - 1; x++) {
                if (input[y][x] == '*') {

                    List<Pair<Integer, Integer>> adjacentNumbers = new ArrayList<>();

                    for (int j = Math.max(0, y - 1); j <= Math.min(139, y + 1); j++) {
                        boolean skipNext = false;
                        for (int i = Math.max(0, x - 1); i <= Math.min(139, x + 1); i++) {
                            if (input[j][i] >= '0' && input[j][i] <= '9') {
                                if (skipNext) {
                                    continue;
                                }
                                skipNext = true;
                                adjacentNumbers.add(new Pair<>(j, i));
                            } else {
                                skipNext = false;
                            }
                        }
                    }

                    if (adjacentNumbers.size() != 2) continue;

                    int ratio = 1;

                    for (Pair<Integer, Integer> adjacentNumber : adjacentNumbers) {
                        int startIndex = adjacentNumber.second();

                        while (input[adjacentNumber.first()][Math.max(0, startIndex - 1)] >= '0' && input[adjacentNumber.first()][Math.max(0, startIndex - 1)] <= '9') {
                            startIndex--;
                            if (startIndex < 0) {
                                startIndex = 0;
                                break;
                            }
                        }

                        int numberLength = 0;
                        int index = 0;

                        try {
                            do {
                                numberLength++;
                                index++;
                            } while (input[adjacentNumber.first()][startIndex + index] >= '0' && input[adjacentNumber.first()][startIndex + index] <= '9');
                        } catch (ArrayIndexOutOfBoundsException ignored) {}

                        StringBuilder number = new StringBuilder();

                        for (int i = startIndex; i < startIndex + numberLength; i++) {
                            number.append(input[adjacentNumber.first()][i]);
                        }

                        ratio *= Integer.parseInt(number.toString());
                    }

                    output += ratio;
                }
            }
        }
        return String.valueOf(output);
    }

    @Override
    protected String day() {
        return "day_3";
    }
}
