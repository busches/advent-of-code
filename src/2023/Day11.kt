package `2023`

import println
import readInput
import kotlin.math.absoluteValue

fun main() {

    fun expandSpace(input: List<String>, expansionSize: Int): List<List<Char>> {
        var updatedSpace = mutableListOf<CharSequence>()

        for (lineNumber in (input.size - 1) downTo 0) {
            val line = input[lineNumber]
            // Convert everything to StringBuilders for Part 2 efficiency
            updatedSpace.add(0, StringBuilder(line))
        }

        "Starting columns ${input.first().length}".println()
        for (columnNumber in (input.first().length - 1) downTo 0) {
            val onlySpace = input.map { it[columnNumber] }.all { it == '.' }
            if (onlySpace) {
                updatedSpace = updatedSpace.map { line ->
                    (line as StringBuilder).insert(columnNumber, ".".repeat(expansionSize))
                    line
                }.toMutableList()
            }
        }
        "Updated columns ${updatedSpace.first().length}".println()

        var updatedSpace2 = updatedSpace.map { it.toList() }.toMutableList()

        "Starting rows ${input.size}".println()
        // Add in new rows
        for (lineNumber in (input.size - 1) downTo 0) {
            val line = input[lineNumber]
            val onlySpace = line.all { it == '.' }
            if (onlySpace) {
                updatedSpace2.addAll(lineNumber, List(expansionSize) { _ -> updatedSpace2[lineNumber] })
            }
        }
        "Updated rows ${updatedSpace2.size}".println()

        return updatedSpace2//.also { it.joinToString("\n").println() }
    }

//    check(expandSpace(readInput("2023/Day11_Test"), 1) == readInput("2023/Day11_TestExpanded"))

    fun part1(input: List<String>): Long {
        val spaceGrid = expandSpace(input, 1)

        // Find Galaxies
        val galaxies = mutableListOf<Pair<Long, Long>>()
        for (x in spaceGrid.indices) {
            for (y in spaceGrid[x].indices) {
                if (spaceGrid[x][y] == '#') {
                    galaxies.add(x.toLong() to y.toLong())
                }
            }
        }

        // Create Pairs
        val galaxyPairs = mutableListOf<Pair<Pair<Long, Long>, Pair<Long, Long>>>()
        for (startingPairIndex in 0..<galaxies.size) {
            for (nextPairIndex in (startingPairIndex + 1)..<galaxies.size) {
                galaxyPairs.add(galaxies[startingPairIndex] to galaxies[nextPairIndex])
            }
        }

        // Find distance between pairs, using straight turns only
        val distances = galaxyPairs.sumOf { (galaxy1, galaxy2) ->
            val (x1, y1) = galaxy1
            val (x2, y2) = galaxy2
            (x2 - x1).absoluteValue + (y2 - y1).absoluteValue
        }

        return distances
    }

    check(part1(readInput("2023/Day11_Test")) == 374L)

    val input = readInput("2023/Day11")
    part1(input).println()

    fun part2(input: List<String>): Long {
        val spaceGrid = expandSpace(input, 1_000_000)

        "Created spaceGrid ${spaceGrid.size}x ${spaceGrid.first().size}".println()

        // Find Galaxies
        val galaxies = mutableListOf<Pair<Long, Long>>()
        for (x in spaceGrid.indices) {
            for (y in spaceGrid[x].indices) {
                if (spaceGrid[x][y] == '#') {
                    galaxies.add(x.toLong() to y.toLong())
                }
            }
        }

        "Found Galaxies - ${galaxies.size}".println()

        // Create Pairs
        val galaxyPairs = mutableListOf<Pair<Pair<Long, Long>, Pair<Long, Long>>>()
        for (startingPairIndex in 0..<galaxies.size) {
            for (nextPairIndex in (startingPairIndex + 1)..<galaxies.size) {
                galaxyPairs.add(galaxies[startingPairIndex] to galaxies[nextPairIndex])
            }
        }

        "Created pairs - ${galaxyPairs.size}".println()

        // Find distance between pairs, using straight turns only
        val distances = galaxyPairs.sumOf { (galaxy1, galaxy2) ->
            val (x1, y1) = galaxy1
            val (x2, y2) = galaxy2
            (x2 - x1).absoluteValue + (y2 - y1).absoluteValue
        }

        return distances
    }
    part2(input).println()
}
