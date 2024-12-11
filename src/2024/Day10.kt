package `2024`

import println
import readInput
import java.util.*

fun main() {
    val start = System.currentTimeMillis()

    fun solve(input: List<String>): MutableList<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val grid = input.map { row -> row.toList() }
        val trailHeads = buildList {
            grid.forEachIndexed { y, row ->
                row.forEachIndexed { x, topographicScore ->
                    if (topographicScore == '0') {
                        add(x to y)
                    }
                }
            }
        }

        val trailsToSearch = ArrayDeque(trailHeads.map { it to it })
        val trailHeadScores = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        val searchPatterns = listOf(
            0 to 1,
            0 to -1,
            1 to 0,
            -1 to 0,
        )
        while (trailsToSearch.isNotEmpty()) {
            val (startingSpot, currentSpot) = trailsToSearch.removeFirst()
            val currentValue = grid[currentSpot.second][currentSpot.first]
            searchPatterns.forEach { pattern ->
                val newSpot = currentSpot + pattern
                if (newSpot.first in grid[0].indices && newSpot.second in grid.indices) {
                    val newSpotValue = grid[newSpot.second][newSpot.first]
                    if (newSpotValue.digitToInt() == currentValue.digitToInt() + 1) {
                        if (newSpotValue == '9') {
                            trailHeadScores.add(startingSpot to newSpot)
                        } else {
                            trailsToSearch.addFirst(startingSpot to newSpot)
                        }
                    }
                }
            }
        }
        return trailHeadScores
    }

    fun part1(input: List<String>): Int {
        val trailHeadScores = solve(input)

        return trailHeadScores.distinct().size
    }


    fun part2(input: List<String>): Int {
        val trailHeadScores = solve(input)

        return trailHeadScores.size
    }

    val sampleInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()
    check(part1(sampleInput) == 36)

    val input = readInput("2024/Day10")
    part1(input).println()

    check(part2(sampleInput) == 81)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}

private operator fun Pair<Int, Int>.plus(currentPosition: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + currentPosition.first to this.second + currentPosition.second
}