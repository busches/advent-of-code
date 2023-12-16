package `2023`

import println
import readInput
import java.time.LocalTime

enum class Rotation(val rotation: Pair<Int, Int>) {
    NORTH(1 to 0),
    WEST(0 to 1),
}

fun main() {

    fun shiftRocks(grid: MutableList<MutableList<Char>>, direction: Rotation): MutableList<MutableList<Char>> {

        val rowSize = grid.indices
        val columnSize = grid.first().indices
        val (shiftX, shiftY) = direction.rotation
        for (row in rowSize) {
            for (column in grid[row].indices) {
                val rockType = grid[row][column]
//                "Found $rockType at $row,$column".println()
                if (rockType == 'O') {
                    var currentRockCoordinates = row to column
                    while (currentRockCoordinates.first - shiftX in rowSize &&
                        currentRockCoordinates.second - shiftY in columnSize &&
                        grid[currentRockCoordinates.first - shiftX][currentRockCoordinates.second - shiftY] == '.'
                    ) {
                        grid[currentRockCoordinates.first - shiftX][currentRockCoordinates.second - shiftY] = 'O'
                        grid[currentRockCoordinates.first][currentRockCoordinates.second] = '.'
                        currentRockCoordinates =
                            currentRockCoordinates.first - shiftX to currentRockCoordinates.second - shiftY
                    }
                }
            }
        }

//        shiftedGrid.joinToString("\n") { it.joinToString("") }.println()
        return grid
    }

    fun northBeamWeight(shiftedGrid: List<List<Char>>) =
        shiftedGrid.reversed()
            .map { it.count { it == 'O' } }
            .mapIndexed { rowIndex, count ->
                (rowIndex + 1) * count
            }
            .sum()
            .also { "${LocalTime.now()} - $it".println() }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toMutableList() }.toMutableList()

        val shiftedGrid = shiftRocks(grid, Rotation.NORTH)

        return northBeamWeight(shiftedGrid)
    }

    check(part1(readInput("2023/Day14_Test")) == 136)

    val input = readInput("2023/Day14")
    part1(input).println()

    fun part2(input: List<String>): Int {
        var grid = input.map { it.toMutableList() }.toMutableList()

        val cache = mutableMapOf(grid.map { it.toList() }.toList() to 0)

        for (iteration in 1..1_000_000_000) {
            grid = shiftRocks(grid, Rotation.NORTH)
            grid = shiftRocks(grid, Rotation.WEST)
            grid = shiftRocks(
                grid.reversed() as MutableList<MutableList<Char>>,
                Rotation.NORTH
            ).reversed() as MutableList<MutableList<Char>> // reverse it and do north, as we always go top down
            grid = shiftRocks(
                grid.map { it.reversed() as MutableList<Char> } as MutableList<MutableList<Char>>, Rotation.WEST)
                .map { it.reversed() as MutableList<Char> } as MutableList<MutableList<Char>> // reverse it and do west, as we always go left to right

            val gridIteration = cache.getOrPut(grid.map { it.toList() }.toList()) { iteration }
            cache.map { (key, value) ->
                value.println(); key.hashCode().println(); key.joinToString("\n") { it.joinToString("") }.println()
            }

            if (iteration != gridIteration) {
                "Found repeated state at $iteration - it was $gridIteration".println()
                return northBeamWeight(cache.keys.elementAt(gridIteration + (1_000_000_000 - iteration) % (iteration - gridIteration)))
            }
        }
        return northBeamWeight(grid)
    }

    check(part2(readInput("2023/Day14_Test")) == 64).also { "Check for part 2 passed".println() }

    part2(input).println()
}
