package `2023`

import println
import readInput

fun main() {
    check(Day18().part1(readInput("2023/Day18_Test")) == 62L).also { "Check Part1 passed".println() }
    Day18().part1(readInput("2023/Day18")).println()

    check(Day18().part2(readInput("2023/Day18_Test")) == 952408144115L).also { "Check Part2 passed".println() }
    Day18().part2(readInput("2023/Day18")).println()
}

class Day18 {
    fun part1(input: List<String>): Long {
        val directions = parseInput(input)

        return solve(directions)
    }

    fun part2(input: List<String>): Long {
        val directions = parseInputPart2(input)

        return solve(directions)
    }

    private fun solve(directions: List<Directions>): Long {
        var currentX = 0
        var currentY = 0
        val coordinates = mutableListOf(0 to 0)
        var steps = 0

        directions.forEach { (direction, moves) ->
            currentY += direction.changeY * moves
            currentX += direction.changeX * moves
            coordinates.add(currentX to currentY)
            steps += moves
        }

        // Area of a Polygon - https://www.wikihow.com/Calculate-the-Area-of-a-Polygon
        val area = coordinates.zipWithNext()
            .sumOf { (first, second) ->
                val (x1, y1) = first
                val (x2, y2) = second

                (x1 * y2.toLong()) - (x2 * y1.toLong())
            } / 2
        // Take the perimeter and divide by 2 to get the new area we have to add, I have no clue why it's +1 but it works
        val perimeter = steps / 2 + 1
        return area + perimeter
    }

    private data class Directions(val direction: Direction, val moves: Int)

    private fun parseInput(input: List<String>): List<Directions> {
        val lineData = "(.) (\\d+)".toRegex()
        return input.map { line ->
            val (direction, moves) = lineData.find(line)!!.destructured
            Directions(Direction.valueOf(direction), moves.toInt())
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun parseInputPart2(input: List<String>): List<Directions> {
        val lineData = ".*\\(#(.*)\\)".toRegex()
        return input.map { line ->
            val (rgbColor) = lineData.find(line)!!.destructured
            val direction = when (rgbColor.last()) {
                '0' -> Direction.R
                '1' -> Direction.D
                '2' -> Direction.L
                '3' -> Direction.U
                else -> throw IllegalArgumentException("What is this ${rgbColor.last()}")
            }
            val moves = rgbColor.dropLast(1).hexToInt()

            Directions(direction, moves)
        }
    }

    private enum class Direction(val changeY: Int, val changeX: Int) {
        U(-1, 0),
        D(1, 0),
        L(0, -1),
        R(0, 1),
    }
}
