package `2023`

import println
import readInput
import java.time.LocalTime

fun main() {


    data class AllTheseMaps(
        val seeds: Sequence<Long>,
        val seedToSoil: List<Triple<Long, Long, Long>>,
        val soilToFertilizer: List<Triple<Long, Long, Long>>,
        val fertilizerToWater: List<Triple<Long, Long, Long>>,
        val waterToLight: List<Triple<Long, Long, Long>>,
        val lightToTemperature: List<Triple<Long, Long, Long>>,
        val temperatureToHumidity: List<Triple<Long, Long, Long>>,
        val humidityToLocation: List<Triple<Long, Long, Long>>,
    )

    fun extractInts(
        input: String
    ): Sequence<Long> = "(\\d+)".toRegex().findAll(input)
        .map { it.value }
        .map { it.toLong() }

    fun extractValues(input: List<String>): AllTheseMaps {
        val seedToSoil = mutableListOf<Triple<Long, Long, Long>>()
        val soilToFertilizer = mutableListOf<Triple<Long, Long, Long>>()
        val fertilizerToWater = mutableListOf<Triple<Long, Long, Long>>()
        val waterToLight = mutableListOf<Triple<Long, Long, Long>>()
        val lightToTemperature = mutableListOf<Triple<Long, Long, Long>>()
        val temperatureToHumidity = mutableListOf<Triple<Long, Long, Long>>()
        val humidityToLocation = mutableListOf<Triple<Long, Long, Long>>()

        val seeds = extractInts(input.first())

        val mapLine = "(\\d+) (\\d+) (\\d+)".toRegex()

        var mapToAddTo: MutableList<Triple<Long, Long, Long>>? = null
        input.drop(1).filter { it.isNotBlank() }.forEach { line ->
            when {
                line.startsWith("seed-to-soil") -> {
                    mapToAddTo = seedToSoil
                }

                line.startsWith("soil-to-fertilizer") -> {
                    mapToAddTo = soilToFertilizer
                }

                line.startsWith("fertilizer-to-water") -> {
                    mapToAddTo = fertilizerToWater
                }

                line.startsWith("water-to-light") -> {
                    mapToAddTo = waterToLight
                }

                line.startsWith("light-to-temperature") -> {
                    mapToAddTo = lightToTemperature
                }

                line.startsWith("temperature-to-humidity") -> {
                    mapToAddTo = temperatureToHumidity
                }

                line.startsWith("humidity-to-location") -> {
                    mapToAddTo = humidityToLocation
                }

                line.first().isDigit() -> {
                    val (destinationRangeStart, sourceRangeStart, rangeLength) = mapLine.find(line)!!.destructured
                    mapToAddTo!!.add(
                        Triple(
                            sourceRangeStart.toLong(),
                            destinationRangeStart.toLong(),
                            rangeLength.toLong()
                        )
                    )
                }
            }
        }


        return AllTheseMaps(
            seeds,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation
        )
    }

    fun lookup(slot: Long, ranges: List<Triple<Long, Long, Long>>): Long {
        ranges.forEach { (sourceRangeStart, destinationRangeStart, rangeLength) ->
            if (slot >= sourceRangeStart && slot < sourceRangeStart + rangeLength) {
                return ((slot - sourceRangeStart) + destinationRangeStart)
            }
        }
        return slot
    }

    fun part1(input: List<String>): Long {
        val (seeds, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation) = extractValues(
            input
        )

        return seeds
            .map { seed -> lookup(seed, seedToSoil) }
            .map { seed -> lookup(seed, soilToFertilizer) }
            .map { seed -> lookup(seed, fertilizerToWater) }
            .map { seed -> lookup(seed, waterToLight) }
            .map { seed -> lookup(seed, lightToTemperature) }
            .map { seed -> lookup(seed, temperatureToHumidity) }
            .map { seed -> lookup(seed, humidityToLocation) }
            .toList()
            .min()
    }

    val testInput = listOf(
        "seeds: 79 14 55 13",
        "",
        "seed-to-soil map:",
        "50 98 2",
        "52 50 48",
        "",
        "soil-to-fertilizer map:",
        "0 15 37",
        "37 52 2",
        "39 0 15",
        "",
        "fertilizer-to-water map:",
        "49 53 8",
        "0 11 42",
        "42 0 7",
        "57 7 4",
        "",
        "water-to-light map:",
        "88 18 7",
        "18 25 70",
        "",
        "light-to-temperature map:",
        "45 77 23",
        "81 45 19",
        "68 64 13",
        "",
        "temperature-to-humidity map:",
        "0 69 1",
        "1 0 69",
        "",
        "humidity-to-location map:",
        "60 56 37",
        "56 93 4"
    )
    check(part1(testInput) == 35L)

    val input = readInput("2023/Day05")
    part1(input).println()


    fun part2(input: List<String>): Long {
        val (seeds, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation) = extractValues(
            input
        )

        return seeds.chunked(2).minOf { (seedStart, rangeLength) ->
            (0..rangeLength).minOf { index ->
                if (index == 0L) {
                    "Processing $seedStart ${LocalTime.now()}".println()
                }
                (seedStart + index)
                    .let { seed -> lookup(seed, seedToSoil) }
                    .let { seed -> lookup(seed, soilToFertilizer) }
                    .let { seed -> lookup(seed, fertilizerToWater) }
                    .let { seed -> lookup(seed, waterToLight) }
                    .let { seed -> lookup(seed, lightToTemperature) }
                    .let { seed -> lookup(seed, temperatureToHumidity) }
                    .let { seed -> lookup(seed, humidityToLocation) }
            }

        }
    }

    check(part2(testInput) == 46L)
    part2(input).println()
}
