package `2023`

import println
import readInput

fun main() {
    check(Day18().part1(readInput("2023/Day18_Test")) == 62).also { "Check Part1 passed".println() }
    Day18().part1(readInput("2023/Day18")).println()

    check(Day18().part2(readInput("2023/Day18_Test")) == 94).also { "Check Part2 passed".println() }
    Day18().part2(readInput("2023/Day18")).println()
}

class Day18 {
    fun part1(input: List<String>): Int {
        val directions = parseInput(input)

        // Do this rough so we allocate enough spots and don't have to resize later
        val gridHeight = directions.filter { (direction, _, _) -> direction == Direction.D || direction == Direction.U }
            .sumOf { (_, moves, _) -> moves } / 2 + 1

        val gridWidth = directions.filter { (direction, _, _) -> direction == Direction.L || direction == Direction.R }
            .sumOf { (_, moves, _) -> moves } / 2

        "Grid is ${gridWidth}x$gridHeight".println()


        val grid = MutableList(gridHeight) { MutableList(gridWidth) { _ -> false } }
        var currentX = 0
        var currentY = 0

        directions.forEach { (direction, moves, _) ->
            for (i in 1..moves) {
                currentY += direction.changeY
                currentX += direction.changeX
                "Moving ${direction} for $moves at $currentY,$currentX, move $i".println()
                grid[currentY][currentX] = true
            }
        }

        // grid.joinToString("\n") { it.map { if (it) "#" else "." }.joinToString("") }.println()

        // Need to count all edges and inside
        val path = mutableListOf<Pair<Int, Int>>()
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x]) {
                    path.add(y to x)
                }
            }
        }


        var pointsInsideTheGrid = 0
        for ((y, line) in grid.withIndex()) {
            for ((x, _) in line.withIndex()) {
                if (y to x in path) { // Edges are counted
                    pointsInsideTheGrid++
                    continue
                }

                // Loop from current point's x to edge of grid
                var intersections = 0
                for (offset in 0..<(line.size - x)) {
                    val currentCoordinate = y to x + offset
                    if (currentCoordinate in path) {
                        intersections++
                    }
                }
                if (intersections % 2 == 1) {
                    pointsInsideTheGrid += 1
                }
            }
        }
        return pointsInsideTheGrid
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    data class Directions(val direction: Direction, val moves: Int, val rgbColor: String)

    private fun parseInput(input: List<String>): List<Directions> {
        val lineData = "(.) (\\d+) \\(#(.*)\\)".toRegex()
        return input.map { line ->
            val (direction, moves, rgbColor) = lineData.find(line)!!.destructured
            Directions(Direction.valueOf(direction), moves.toInt(), rgbColor)
        }
    }

    enum class Direction(val changeY: Int, val changeX: Int) {
        U(-1, 0),
        D(1, 0),
        L(0, -1),
        R(0, 1),
    }
}
