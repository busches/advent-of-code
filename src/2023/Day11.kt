package `2023`

import println
import readInput
import kotlin.math.absoluteValue

fun main() {

    fun expandSpace(input: List<String>): List<String> {
        var updatedSpace = mutableListOf<String>()

        // Add in new rows first
        for (lineNumber in (input.size - 1) downTo 0) {
            val line = input[lineNumber]
            updatedSpace.add(0, line)
            val onlySpace = line.all { it == '.' }
            if (onlySpace) {
                updatedSpace.add(0, line)
            }
        }

        // Now add in columns
        for (columnNumber in (input.first().length - 1) downTo 0) {
            val onlySpace = input.map { it[columnNumber] }.all { it == '.' }
            if (onlySpace) {
                updatedSpace = updatedSpace.map { line -> StringBuilder(line).insert(columnNumber, '.').toString() }
                    .toMutableList()
            }
        }

        return updatedSpace//.also { it.joinToString("\n").println() }
    }

    check(expandSpace(readInput("2023/Day11_Test")) == readInput("2023/Day11_TestExpanded"))

    fun part1(input: List<String>): Int {
        val spaceGrid = expandSpace(input).map { it.toList() }

        // Find Galaxies
        val galaxies = mutableListOf<Pair<Int, Int>>()
        for (x in spaceGrid.indices) {
            for (y in spaceGrid[x].indices) {
                if (spaceGrid[x][y] == '#') {
                    galaxies.add(x to y)
                }
            }
        }

        // Create Pairs
        val galaxyPairs = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
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

    check(part1(readInput("2023/Day11_Test")) == 374)

    val input = readInput("2023/Day11")
    part1(input).println()

    fun part2(input: List<String>): Int {
        TODO()
    }
    part2(input).println()
}
