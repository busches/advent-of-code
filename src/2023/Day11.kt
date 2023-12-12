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

        for (columnNumber in (input.first().length - 1) downTo 0) {
            val onlySpace = input.map { it[columnNumber] }.all { it == '.' }
            if (onlySpace) {
                updatedSpace = updatedSpace.map { line ->
                    (line as StringBuilder).insert(columnNumber, ".".repeat(expansionSize))
                    line
                }.toMutableList()
            }
        }

        val updatedSpace2 = updatedSpace.map { it.toList() }.toMutableList()

        // Add in new rows
        for (lineNumber in (input.size - 1) downTo 0) {
            val line = input[lineNumber]
            val onlySpace = line.all { it == '.' }
            if (onlySpace) {
                updatedSpace2.addAll(lineNumber, List(expansionSize) { _ -> emptyList() })
            }
        }
        return updatedSpace2//.also { it.joinToString("\n").println() }
    }

//    check(expandSpace(readInput("2023/Day11_Test"), 1) == readInput("2023/Day11_TestExpanded"))

    fun findGalaxies(spaceGrid: List<List<Char>>): MutableList<Pair<Long, Long>> {
        val galaxies = mutableListOf<Pair<Long, Long>>()
        for (x in spaceGrid.indices) {
            for (y in spaceGrid[x].indices) {
                if (spaceGrid[x][y] == '#') {
                    galaxies.add(x.toLong() to y.toLong())
                }
            }
        }
        return galaxies
    }

    fun createPairs(galaxies: List<Pair<Long, Long>>): List<Pair<Pair<Long, Long>, Pair<Long, Long>>> {
        val galaxyPairs = mutableListOf<Pair<Pair<Long, Long>, Pair<Long, Long>>>()
        for (startingPairIndex in 0..<galaxies.size) {
            for (nextPairIndex in (startingPairIndex + 1)..<galaxies.size) {
                galaxyPairs.add(galaxies[startingPairIndex] to galaxies[nextPairIndex])
            }
        }
        return galaxyPairs
    }

    fun solve(input: List<String>, expansionSize: Int): Long {
        val originalSpaceGrid = input.map { it.toList() }
        val newSpaceGrid = expandSpace(input, 1)

        val originalGalaxies = findGalaxies(originalSpaceGrid)
        val updatedGalaxies = findGalaxies(newSpaceGrid)

        val translatedGalaxies = updatedGalaxies.mapIndexed { index, updatedGalaxy ->
            val (x1, y1) = originalGalaxies[index]
            val (x2, y2) = updatedGalaxy

            val updatedX = (x2 - x1).times(expansionSize) + x1
            val updatedY = (y2 - y1).times(expansionSize) + y1

            updatedX to updatedY
        }

        val galaxyPairs = createPairs(translatedGalaxies)

        // Find distance between pairs, using straight turns only
        val distances = galaxyPairs.sumOf { (galaxy1, galaxy2) ->
            val (x1, y1) = galaxy1
            val (x2, y2) = galaxy2
            (x2 - x1).absoluteValue + (y2 - y1).absoluteValue
        }

        return distances
    }

    fun part1(input: List<String>): Long {
        return solve(input, 1)
    }

    check(part1(readInput("2023/Day11_Test")) == 374L)

    val input = readInput("2023/Day11")
    part1(input).println()

    fun part2(input: List<String>): Long {
        // 591011260421 is too low
        // 613687601106 is too high
       return solve(input, 1_000_000)
    }
    part2(input).println()
}
