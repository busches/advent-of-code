package `2023`

import println
import readInput

fun main() {

    fun shiftRocksNorth(grid: List<List<Char>>): MutableList<MutableList<Char>> {
        val shiftedGrid = grid.map { it.toMutableList() }.toMutableList()

        for (row in 1..<grid.size) {
            for (column in grid[row].indices) {
                val rockType = shiftedGrid[row][column]
//                "Found $rockType at $row,$column".println()
                if (rockType == 'O') {
                    // North is shift 1, 0
                    // East is shift 0,1
                    // South is shift -1, 0
                    // West is shift 0, -1

                    val (shiftX, shiftY) = 1 to 0


                    var currentRockCoordinates = row to column
                    while (currentRockCoordinates.first - shiftX >= 0 && currentRockCoordinates.first - shiftX < grid.size &&
                        currentRockCoordinates.second - shiftY >= 0 && currentRockCoordinates.second - shiftY < grid.first().size &&
                        shiftedGrid[ currentRockCoordinates.first - shiftX][currentRockCoordinates.second - shiftY] == '.') {
                        shiftedGrid[currentRockCoordinates.first - shiftX][currentRockCoordinates.second - shiftY] = 'O'
                        shiftedGrid[currentRockCoordinates.first][currentRockCoordinates.second] = '.'
                        currentRockCoordinates = currentRockCoordinates.first - shiftX to currentRockCoordinates.second - shiftY
                    }
                }
            }
        }

        shiftedGrid.joinToString("\n") { it.joinToString("") }.println()
        return shiftedGrid
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }

        val shiftedGrid = shiftRocksNorth(grid)

        return shiftedGrid.reversed()
            .map { it.count { it == 'O' } }
            .mapIndexed { rowIndex, count ->
                (rowIndex + 1) * count
            }.sum()
    }

    check(part1(readInput("2023/Day14_Test")) == 136)

    val input = readInput("2023/Day14")
    part1(input).println()

    fun part2(input: List<String>): Int {
        TODO()

    }

    check(part2(readInput("2023/Day14_Test")) == 64)


    part2(input).println()
}
