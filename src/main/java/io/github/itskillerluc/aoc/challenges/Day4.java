package io.github.itskillerluc.aoc.challenges;

import io.github.itskillerluc.aoc.Challenge;
import io.github.itskillerluc.aoc.utility.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 extends Challenge {
    private static Map<Integer, Integer> cache = new HashMap<>();

    @Override
    protected String part1() throws URISyntaxException, IOException {
        int result = 0;
        for (String input : getInputList()) {
            String[] data = input.split(":")[1].stripLeading().split("\\|");
            List<Integer> keys = Arrays.stream(data[0].split("\\s+")).map(Integer::parseInt).toList();
            long wins = Arrays.stream(data[1].stripLeading().split("\\s+")).map(Integer::parseInt).filter(keys::contains).count() - 1;
            double prize = wins >= 0 ? Math.pow(2, wins) : 0;
            result += prize;
        }
        return String.valueOf(result);
    }

    @Override
    protected String part2() throws URISyntaxException, IOException {
        for (int i = getInputList().size() - 1; i >= 0; i--) {
            AtomicLong cachedValue = new AtomicLong(0);
            scratchAllCards(cachedValue, i);
        }
        return String.valueOf(cache.values().stream().mapToInt(e -> e).sum());
    }

    private List<Integer> scratchAllCards(AtomicLong result, int card) throws URISyntaxException, IOException {
        result.getAndIncrement();
        var scratchedCards = scratchCard(card);
        for (Integer scratchedCard : scratchedCards) {
            if (cache.containsKey(scratchedCard)) {
                result.addAndGet(cache.get(scratchedCard));
                continue;
            }
            scratchAllCards(result, scratchedCard);
        }
        cache.putIfAbsent(card, result.intValue());
        return scratchedCards;
    }

    private List<Integer> scratchCard(int card) throws URISyntaxException, IOException {
        String input = getInputList().get(card);
        String[] data = input.split(":")[1].stripLeading().split("\\|");
        List<Integer> keys = Arrays.stream(data[0].split("\\s+")).map(Integer::parseInt).toList();
        long wins = Arrays.stream(data[1].stripLeading().split("\\s+")).map(Integer::parseInt).filter(keys::contains).count();
        if (wins == 0) return List.of();
        return IntStream.range(card + 1, card + (int) wins + 1).boxed().toList();
    }

    @Override
    protected String day() {
        return "day_4";
    }
}
