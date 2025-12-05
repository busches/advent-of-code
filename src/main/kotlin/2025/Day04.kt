package `2025`

import utils.println
import utils.readInput

fun main() {

    fun checkGrid(grid: List<List<Char>>, y: Int, x: Int): Int {
        return if (y in grid.indices && x in grid[y].indices && grid[y][x] == '@') 1 else 0
    }

    fun part1(input: List<String>): Long {
        val grid = input.map { it.toList() }
        var rollsToMove = 0L
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == '@') {
                    val upperLeft = checkGrid(grid, y - 1, x - 1)
                    val upperMiddle = checkGrid(grid, y - 1, x)
                    val upperRight = checkGrid(grid, y - 1, x + 1)
                    val left = checkGrid(grid, y, x - 1)
                    val right = checkGrid(grid, y, x + 1)
                    val lowerLeft = checkGrid(grid, y + 1, x - 1)
                    val lowerMiddle = checkGrid(grid, y + 1, x)
                    val lowerRight = checkGrid(grid, y + 1, x + 1)
                    val nearByRolls =
                        upperLeft + upperMiddle + upperRight + left + right + lowerLeft + lowerMiddle + lowerRight
                    if (nearByRolls < 4) {
                        print("x")
                        rollsToMove++
                    } else {
                        print(grid[y][x])
                    }
                } else {
                    print(grid[y][x])
                }
            }
            println()
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
                    if (grid[y][x] == '@') {
                        val upperLeft = checkGrid(grid, y - 1, x - 1)
                        val upperMiddle = checkGrid(grid, y - 1, x)
                        val upperRight = checkGrid(grid, y - 1, x + 1)
                        val left = checkGrid(grid, y, x - 1)
                        val right = checkGrid(grid, y, x + 1)
                        val lowerLeft = checkGrid(grid, y + 1, x - 1)
                        val lowerMiddle = checkGrid(grid, y + 1, x)
                        val lowerRight = checkGrid(grid, y + 1, x + 1)
                        val nearByRolls =
                            upperLeft + upperMiddle + upperRight + left + right + lowerLeft + lowerMiddle + lowerRight
                        if (nearByRolls < 4) {
                            print("x")
                            rollsRemoved++
                            rollsRemoveThisRun++
                            grid[y][x] = 'x'
                        } else {
                            print(grid[y][x])
                        }
                    } else {
                        print(grid[y][x])
                    }
                }
                println()
            }
        }

        return rollsRemoved
    }

    val input = readInput("2025/Day04")
    part1(input).println()

    check(part2(sampleInput.lines()) == 43L)

    part2(input).println()
}
