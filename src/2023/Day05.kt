package `2023`

import println
import readInput

fun main() {


    data class AllTheseMaps(
        val seeds: List<Long>,
        val seedToSoil: Map<Long, Long>,
        val soilToFertilizer: Map<Long, Long>,
        val fertilizerToWater: Map<Long, Long>,
        val waterToLight: Map<Long, Long>,
        val lightToTemperature: Map<Long, Long>,
        val temperatureToHumidity: Map<Long, Long>,
        val humidityToLocation: Map<Long, Long>,
    )

    fun extractInts(
        input: String
    ) = "(\\d+)".toRegex().findAll(input)
        .map { it.value }
        .map { it.toLong() }
        .toList()

    fun extractValues(input: List<String>): AllTheseMaps {
        val seedToSoil = mutableMapOf<Long, Long>()
        val soilToFertilizer = mutableMapOf<Long, Long>()
        val fertilizerToWater = mutableMapOf<Long, Long>()
        val waterToLight = mutableMapOf<Long, Long>()
        val lightToTemperature = mutableMapOf<Long, Long>()
        val temperatureToHumidity = mutableMapOf<Long, Long>()
        val humidityToLocation = mutableMapOf<Long, Long>()

        val seeds = extractInts(input.first())

        val mapLine = "(\\d+) (\\d+) (\\d+)".toRegex()

        var mapToAddTo: MutableMap<Long, Long>? = null
        input.drop(1).filter { it.isNotBlank() }.forEach { line ->
            line.println()
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
                    (0..<(rangeLength.toInt())).forEach { index ->
                        mapToAddTo!![sourceRangeStart.toLong() + index] = destinationRangeStart.toLong() + index
                    }
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

    fun part1(input: List<String>): Long {
        val (seeds, seedToSoil, soilToFertilizer, fertilizerToWater, waterToLight, lightToTemperature, temperatureToHumidity, humidityToLocation) = extractValues(
            input
        )

        return seeds
            .map { seed -> seedToSoil[seed] ?: seed }
            .map { seed -> soilToFertilizer[seed] ?: seed }
            .map { seed -> fertilizerToWater[seed] ?: seed }
            .map { seed -> waterToLight[seed] ?: seed }
            .map { seed -> lightToTemperature[seed] ?: seed }
            .map { seed -> temperatureToHumidity[seed] ?: seed }
            .map { seed -> humidityToLocation[seed] ?: seed }
            .min()
    }

    check(
        part1(
            listOf(
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
        ) == 35L
    )

    val input = readInput("2023/Day05")
    part1(input).println()


    fun part2(input: List<String>): Int {
        TODO()
    }

    check(
        part2(
            listOf(
                "London to Dublin = 464",
                "London to Belfast = 518",
                "Dublin to Belfast = 141"
            )
        ) == 982
    )


    part2(input).println()
}
