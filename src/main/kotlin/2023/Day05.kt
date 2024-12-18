package `2023`

import extractLongs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import utils.println
import utils.readInput
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


    fun extractValues(input: List<String>): AllTheseMaps {
        val seedToSoil = mutableListOf<Triple<Long, Long, Long>>()
        val soilToFertilizer = mutableListOf<Triple<Long, Long, Long>>()
        val fertilizerToWater = mutableListOf<Triple<Long, Long, Long>>()
        val waterToLight = mutableListOf<Triple<Long, Long, Long>>()
        val lightToTemperature = mutableListOf<Triple<Long, Long, Long>>()
        val temperatureToHumidity = mutableListOf<Triple<Long, Long, Long>>()
        val humidityToLocation = mutableListOf<Triple<Long, Long, Long>>()

        val seeds = input.first().extractLongs()

        val mapLine = "(\\d+) (\\d+) (\\d+)".toRegex()

        // Todo, instead of separate maps, we could make this a List<List<Long>> then chain each list we call, instead of swapping between maps
        var listToAddTo: MutableList<Triple<Long, Long, Long>>? = null
        input.drop(1).filter { it.isNotBlank() }.forEach { line ->
            when {
                line.startsWith("seed-to-soil") -> {
                    listToAddTo = seedToSoil
                }

                line.startsWith("soil-to-fertilizer") -> {
                    listToAddTo = soilToFertilizer
                }

                line.startsWith("fertilizer-to-water") -> {
                    listToAddTo = fertilizerToWater
                }

                line.startsWith("water-to-light") -> {
                    listToAddTo = waterToLight
                }

                line.startsWith("light-to-temperature") -> {
                    listToAddTo = lightToTemperature
                }

                line.startsWith("temperature-to-humidity") -> {
                    listToAddTo = temperatureToHumidity
                }

                line.startsWith("humidity-to-location") -> {
                    listToAddTo = humidityToLocation
                }

                line.first().isDigit() -> {
                    val (destinationRangeStart, sourceRangeStart, rangeLength) = mapLine.find(line)!!.destructured
                    listToAddTo!!.add(
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

    val testInput = readInput("2023/Day05_Test")
    check(part1(testInput) == 35L)

    val input = readInput("2023/Day05")
    part1(input).println()


    fun part2(input: List<String>): Long {
        val (seeds, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation) = extractValues(
            input
        )

        return runBlocking(Dispatchers.Default) {
            seeds.chunked(2)
                .map { (seedStart, rangeLength) ->
                    async {
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
                .toList()
                .awaitAll()
                .min()
                .also { "done ${LocalTime.now()}" }
        }
    }

    check(part2(testInput) == 46L)
    part2(input).println()
}
