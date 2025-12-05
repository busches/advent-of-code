package `2025`

import utils.println
import utils.readInput

fun main() {

    fun hasRoll(grid: List<List<Char>>, y: Int, x: Int): Int {
        return if (y in grid.indices && x in grid[y].indices && grid[y][x] == '@') 1 else 0
    }

    fun canMoveRoll(grid: List<List<Char>>, y: Int, x: Int): Boolean {
        if (grid[y][x] == '@') {
            val coordinatesToSearch = listOf(
                Pair(y - 1, x - 1),
                Pair(y - 1, x),
                Pair(y - 1, x + 1),
                Pair(y, x - 1),
                Pair(y, x + 1),
                Pair(y + 1, x - 1),
                Pair(y + 1, x),
                Pair(y + 1, x + 1),
            )
           return coordinatesToSearch.sumOf { (y, x) -> hasRoll(grid, y, x) } < 4
        }
        return false
    }

    fun part1(input: List<String>): Long {
        val grid = input.map { it.toList() }
        var rollsToMove = 0L
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (canMoveRoll(grid, y, x)) {
                    rollsToMove++
                }
            }
        }

        return rollsToMove
    }

    val sampleInput = """
        ..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.
    """.trimIndent()
    check(part1(sampleInput.lines()) == 13L)

    fun part2(input: List<String>): Long {
        val grid = input.map { it.toMutableList() }
        var rollsRemoved = 0L
        var rollsRemoveThisRun = 1
        while (rollsRemoveThisRun > 0) {
            rollsRemoveThisRun = 0
            for (y in grid.indices) {
                for (x in grid[y].indices) {
                    if (canMoveRoll(grid, y, x)) {
                        rollsRemoved++
                        rollsRemoveThisRun++
                        grid[y][x] = 'x'
                    }
                }
            }
        }

        return rollsRemoved
    }

    val input = readInput("2025/Day04")
    part1(input).println()

    check(part2(sampleInput.lines()) == 43L)

    part2(input).println()
}
