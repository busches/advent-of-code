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
                    for (prevRow in (row - 1) downTo 0) {
                        val prevRock = shiftedGrid[prevRow][column]
//                        "  Found $prevRock at $prevRow,$column".println()
                        if (prevRock == '.') {
                            shiftedGrid[prevRow][column] = 'O'
                            shiftedGrid[prevRow + 1][column] = '.'
//                            "  Swapped rocks".println()
//                            shiftedGrid.joinToString("\n") { it.joinToString("") }.println()
                        } else {
//                            "  Not swapping".println()
                            break
                        }
                    }
                }
            }
        }

//        shiftedGrid.joinToString("\n") { it.joinToString("") }.println()
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

    check(part2(readInput("2023/Day14_Test")) == 145)


    part2(input).println()
}
