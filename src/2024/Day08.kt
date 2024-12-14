package `2024`

import println
import readInput
import utils.combinations

fun main() {
    data class Coordinate(val x: Int, val y: Int) {
        operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
        operator fun minus(other: Coordinate) = Coordinate((x - other.x), (y - other.y))
        operator fun times(multiplier: Int): Coordinate = Coordinate(x * multiplier, y * multiplier)
    }

    val start = System.currentTimeMillis()

    fun part1CalculateAntennas(first: Coordinate, second: Coordinate, maxX: Int, maxY: Int): List<Coordinate> {
        val difference = (first - second)
        val firstAntenna = first + difference
        val secondAntenna = second - difference

        return listOf(firstAntenna, secondAntenna)
            .filter { (x, y) -> x in 0..<maxX && y in 0..<maxY }
    }

    fun part2CalculateAntennas(first: Coordinate, second: Coordinate, maxX: Int, maxY: Int): List<Coordinate> {
        val difference = (first - second)
        val antennas = mutableListOf(first, second) // Both coordinates always are antennas now

        var iterations = 1
        while (true) {
            val antenna = first + difference * iterations
            if (antenna.x in 0..<maxX && antenna.y in 0..<maxY) {
                antennas.add(antenna)
                iterations++
            } else {
                break
            }
        }

        iterations = 1
        while (true) {
            val antenna = first - difference * iterations
            if (antenna.x in 0..<maxX && antenna.y in 0..<maxY) {
                antennas.add(antenna)
                iterations++
            } else {
                break
            }
        }

        return antennas
    }

    fun solve(input: List<String>, calculateAntennas: (Coordinate, Coordinate, Int, Int) -> List<Coordinate>): Int {
        val map = input.map { it.toList() }
        val antiNodes = mutableMapOf<Char, MutableList<Coordinate>>()
        map.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c != '.') {
                    antiNodes.computeIfAbsent(c) { mutableListOf() }.apply {
                        add(Coordinate(x, y))
                    }
                }
            }
        }

        val maxY = map.size
        val maxX = map[0].size

        return antiNodes
            .flatMap { (_, coordinates) ->
                coordinates.combinations().flatMap { (first, second) -> calculateAntennas(first, second, maxX, maxY) }
            }
            .toSet() // Some towers produce antennas at the same spot and we don't want to count them
            .count()
    }

    fun part1(input: List<String>): Int {
        return solve(input, ::part1CalculateAntennas)
    }


    fun part2(input: List<String>): Int {
        return solve(input, ::part2CalculateAntennas)
    }

    val sampleInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()
    val sampleInput1 = """
        ..........
        ..........
        ..........
        ....a.....
        ........a.
        .....a....
        ..........
        ......A...
        ..........
        ..........
    """.trimIndent().lines()
    check(part1(sampleInput1) == 4)
    check(part1(sampleInput) == 14)

    val input = readInput("2024/Day08")
    part1(input).println()


    val sampleForPart2 = """
        T.........
        ...T......
        .T........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
        ..........
    """.trimIndent().lines()
    check(part2(sampleForPart2) == 9)
    check(part2(sampleInput) == 34)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
