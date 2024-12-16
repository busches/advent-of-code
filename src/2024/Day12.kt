package `2024`

import println
import readInput

fun main() {
    data class Coordinate(val x: Int, val y: Int) {
        operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
        fun hasBorder(move: Coordinate, regionCoordinates: Set<Coordinate>): Boolean =
            this in regionCoordinates && (this + move) !in regionCoordinates

        fun shiftClockwise(): Coordinate {
            val (x, y) = this
            // Wrote this out, but found the pattern that Left and Right are simple swaps to Up/Down, but Down/Up to Left/Right are inverse
            return Coordinate(y * -1, x)
        }
    }

    val start = System.currentTimeMillis()

    val searchPatterns = listOf(
        Coordinate(0, 1),
        Coordinate(0, -1),
        Coordinate(1, 0),
        Coordinate(-1, 0),
    )

    fun mapRegions(input: List<String>): MutableMap<Coordinate, Int> {
        val regions = mutableMapOf<Coordinate, Int>()
        val map = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    put(Coordinate(x, y), input[y][x])
                }
            }
        }

        var runningRegionNumber = 0
        for ((coordinate, plant) in map) {
            if (coordinate in regions) {
                continue
            }
            runningRegionNumber++
            val spotsToSearch = ArrayDeque<Coordinate>().apply { add(coordinate) }
            while (spotsToSearch.isNotEmpty()) {
                val currentSpot = spotsToSearch.removeFirst()
                if (map[currentSpot] == plant) {
                    regions[currentSpot] = runningRegionNumber
                    val newSpots = searchPatterns.map { currentSpot + it }
                        .filter { nextSpot -> nextSpot !in spotsToSearch && nextSpot in map && nextSpot !in regions }

                    spotsToSearch.addAll(newSpots)
                }
            }
        }
        return regions
    }

    fun part1(input: List<String>): Int {
        val regions = mapRegions(input)

        return (1..regions.size).sumOf { regionNumber ->
            val plants = regions.filter { it.value == regionNumber }
            val regionCoordinates = plants.keys
            val area = plants.size
            // For Perimeter, we can assume each block has 4 sides, unless it's touching another one in the region
            val perimeter = regionCoordinates.sumOf { coordinate ->
                val startingPerimeter = 4
                val touchingSides = searchPatterns
                    .map { searchPattern -> coordinate + searchPattern }
                    .count { touchingCoordinate -> touchingCoordinate in regionCoordinates }
                startingPerimeter - touchingSides
            }
            area * perimeter
        }
    }


    fun part2(input: List<String>): Int {
        val regions = mapRegions(input)

        return (1..regions.size).sumOf { regionNumber ->
            val plants = regions.filter { it.value == regionNumber }
            val regionCoordinates = plants.keys
            val area = plants.size
            // Per wikipedia, we can count the number of sides of an irregular polygon by counting its corners
            val corners = regionCoordinates.sumOf { coordinate ->
                searchPatterns.count { searchPattern ->
                    coordinate.hasBorder(searchPattern, regionCoordinates) &&
                            !(coordinate + searchPattern.shiftClockwise())
                                .hasBorder(searchPattern, regionCoordinates)
                }
            }
            area * corners
        }
    }

    val sampleInput = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent().lines()
    check(part1(sampleInput) == 140)

    val input = readInput("2024/Day12")
    part1(input).println()

    check(part2(sampleInput) == 80)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
