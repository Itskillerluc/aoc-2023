package io.github.itskillerluc.aoc.challenges;

import io.github.itskillerluc.aoc.Challenge;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day5 extends Challenge {

    record converter(long src_start, long dest_start, long length) {
        long convert(long src) {
            long index = src - src_start;
            if (index > length || index < 0) return 0;
            return index + dest_start;
        }
    }

    @Override
    protected String part1() throws URISyntaxException, IOException {
        LongStream seeds = Arrays.stream(getInputList().get(0).replace("seeds: ", "").split(" ")).mapToLong(Long::parseLong);

        int seedToSoilIndex = getIndex("seed-to-soil map:", getInputList());
        int soilToFertilizerIndex = getIndex("soil-to-fertilizer map:", getInputList());
        int fertilizerToWaterIndex = getIndex("fertilizer-to-water map:", getInputList());
        int waterToLightIndex = getIndex("water-to-light map:", getInputList());
        int lightToTemperatureIndex = getIndex("light-to-temperature map:", getInputList());
        int temperatureToHumidityIndex = getIndex("temperature-to-humidity map:", getInputList());
        int humidityToLocationIndex = getIndex("humidity-to-location map:", getInputList());

        List<LongUnaryOperator> seedToSoilOperator = new ArrayList<>();
        List<LongUnaryOperator> soilToFertilizerOperator = new ArrayList<>();
        List<LongUnaryOperator> fertilizerToWaterOperator = new ArrayList<>();
        List<LongUnaryOperator> waterToLightOperator = new ArrayList<>();
        List<LongUnaryOperator> lightToTemperatureOperator = new ArrayList<>();
        List<LongUnaryOperator> temperatureToHumidityOperator = new ArrayList<>();
        List<LongUnaryOperator> humidityToLocationOperator = new ArrayList<>();

        for (int i = seedToSoilIndex + 1; i < soilToFertilizerIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            seedToSoilOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = soilToFertilizerIndex + 1; i < fertilizerToWaterIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            soilToFertilizerOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = fertilizerToWaterIndex + 1; i < waterToLightIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            fertilizerToWaterOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = waterToLightIndex + 1; i < lightToTemperatureIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            waterToLightOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = lightToTemperatureIndex + 1; i < temperatureToHumidityIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            lightToTemperatureOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = temperatureToHumidityIndex + 1; i < humidityToLocationIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            temperatureToHumidityOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = humidityToLocationIndex + 1; i < getInputList().size() ; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            humidityToLocationOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        LongStream soils = seeds.map(seed -> {
            var map = seedToSoilOperator.stream().mapToLong(operator -> operator.applyAsLong(seed)).sum();
            return map == 0 ? seed : map;
        });
        LongStream fertilizers = soils.map(soil -> {
            var map = soilToFertilizerOperator.stream().mapToLong(operator -> operator.applyAsLong(soil)).sum();
            return map == 0 ? soil : map;
        });
        LongStream waters = fertilizers.map(fertilizer -> {
            var map = fertilizerToWaterOperator.stream().mapToLong(operator -> operator.applyAsLong(fertilizer)).sum();
            return map == 0 ? fertilizer : map;
        });
        LongStream lights = waters.map(water -> {
            var map = waterToLightOperator.stream().mapToLong(operator -> operator.applyAsLong(water)).sum();
            return map == 0 ? water : map;
        });
        LongStream temperatures = lights.map(light -> {
            var map = lightToTemperatureOperator.stream().mapToLong(operator -> operator.applyAsLong(light)).sum();
            return map == 0 ? light : map;
        });
        LongStream humidities = temperatures.map(temperature -> {
            var map = temperatureToHumidityOperator.stream().mapToLong(operator -> operator.applyAsLong(temperature)).sum();
            return map == 0 ? temperature : map;
        });
        LongStream locations = humidities.map(humidity -> {
            var map = humidityToLocationOperator.stream().mapToLong(operator -> operator.applyAsLong(humidity)).sum();
            return map == 0 ? humidity : map;
        });

        return String.valueOf(locations.min().orElse(-1));
    }

    int getIndex(String string, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(string)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected String part2() throws URISyntaxException, IOException {
        List<LongStream> seeds = new ArrayList<>();

        var seedNumbers = getInputList().get(0).replace("seeds: ", "").split(" ");
        for (int i = 0; i < seedNumbers.length; i+= 2) {
            seeds.add(LongStream.range(Long.parseLong(seedNumbers[i]), Long.parseLong(seedNumbers[i + 1]) + Long.parseLong(seedNumbers[i])).parallel());
        }

        int seedToSoilIndex = getIndex("seed-to-soil map:", getInputList());
        int soilToFertilizerIndex = getIndex("soil-to-fertilizer map:", getInputList());
        int fertilizerToWaterIndex = getIndex("fertilizer-to-water map:", getInputList());
        int waterToLightIndex = getIndex("water-to-light map:", getInputList());
        int lightToTemperatureIndex = getIndex("light-to-temperature map:", getInputList());
        int temperatureToHumidityIndex = getIndex("temperature-to-humidity map:", getInputList());
        int humidityToLocationIndex = getIndex("humidity-to-location map:", getInputList());

        List<LongUnaryOperator> seedToSoilOperator = new ArrayList<>();
        List<LongUnaryOperator> soilToFertilizerOperator = new ArrayList<>();
        List<LongUnaryOperator> fertilizerToWaterOperator = new ArrayList<>();
        List<LongUnaryOperator> waterToLightOperator = new ArrayList<>();
        List<LongUnaryOperator> lightToTemperatureOperator = new ArrayList<>();
        List<LongUnaryOperator> temperatureToHumidityOperator = new ArrayList<>();
        List<LongUnaryOperator> humidityToLocationOperator = new ArrayList<>();

        for (int i = seedToSoilIndex + 1; i < soilToFertilizerIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            seedToSoilOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = soilToFertilizerIndex + 1; i < fertilizerToWaterIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            soilToFertilizerOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = fertilizerToWaterIndex + 1; i < waterToLightIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            fertilizerToWaterOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = waterToLightIndex + 1; i < lightToTemperatureIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            waterToLightOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = lightToTemperatureIndex + 1; i < temperatureToHumidityIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            lightToTemperatureOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = temperatureToHumidityIndex + 1; i < humidityToLocationIndex - 1; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            temperatureToHumidityOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        for (int i = humidityToLocationIndex + 1; i < getInputList().size() ; i++) {
            String[] mapping = getInputList().get(i).split(" ");
            humidityToLocationOperator.add(new converter(Long.parseLong(mapping[1]), Long.parseLong(mapping[0]), Long.parseLong(mapping[2]))::convert);
        }

        LongStream soils = seeds.stream().flatMapToLong(l -> l).map(seed -> {
            var map = seedToSoilOperator.stream().mapToLong(operator -> operator.applyAsLong(seed)).sum();
            return map == 0 ? seed : map;
        });
        LongStream fertilizers = soils.map(soil -> {
            var map = soilToFertilizerOperator.stream().mapToLong(operator -> operator.applyAsLong(soil)).sum();
            return map == 0 ? soil : map;
        });
        LongStream waters = fertilizers.map(fertilizer -> {
            var map = fertilizerToWaterOperator.stream().mapToLong(operator -> operator.applyAsLong(fertilizer)).sum();
            return map == 0 ? fertilizer : map;
        });
        LongStream lights = waters.map(water -> {
            var map = waterToLightOperator.stream().mapToLong(operator -> operator.applyAsLong(water)).sum();
            return map == 0 ? water : map;
        });
        LongStream temperatures = lights.map(light -> {
            var map = lightToTemperatureOperator.stream().mapToLong(operator -> operator.applyAsLong(light)).sum();
            return map == 0 ? light : map;
        });
        LongStream humidities = temperatures.map(temperature -> {
            var map = temperatureToHumidityOperator.stream().mapToLong(operator -> operator.applyAsLong(temperature)).sum();
            return map == 0 ? temperature : map;
        });
        LongStream locations = humidities.map(humidity -> {
            var map = humidityToLocationOperator.stream().mapToLong(operator -> operator.applyAsLong(humidity)).sum();
            return map == 0 ? humidity : map;
        });

        return String.valueOf(locations.min().orElse(-1));
    }
    @Override
    protected String day() {
        return "day_5";
    }
}
