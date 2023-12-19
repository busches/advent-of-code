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

        // For the full dataset, we have to increase our X, Y offset as we travel left from the start, we don't start at 0,0
        val xOffset = 169
        val yOffset = 150

        // Do this rough so we allocate enough spots and don't have to resize later
        val gridHeight = yOffset + 75
        val gridWidth = xOffset + 340

        return solve(gridWidth, gridHeight, xOffset, yOffset, directions)
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    private fun solve(
        gridWidth: Int,
        gridHeight: Int,
        xOffset: Int,
        yOffset: Int,
        directions: List<Directions>
    ): Int {
        "Grid is ${gridWidth}x$gridHeight".println()

        val grid = MutableList(gridHeight) { MutableList(gridWidth) { _ -> '.' } }
        var currentX = xOffset
        var currentY = yOffset

        directions.forEachIndexed { index, (direction, moves) ->
            val nextDirection = directions[if (index + 1 == directions.size) 0 else index + 1].direction
            for (i in 1..moves) {
                currentY += direction.changeY
                currentX += direction.changeX
                grid[currentY][currentX] = when (direction) {
                    Direction.U -> when {
                        i == moves && nextDirection == Direction.L -> '┐'
                        i == moves && nextDirection == Direction.R -> '┌'
                        else -> '│'
                    }

                    Direction.D -> when {
                        i == moves && nextDirection == Direction.R -> '└'
                        i == moves && nextDirection == Direction.L -> '┘'
                        else -> '│'
                    }

                    Direction.L -> when {
                        i == moves && nextDirection == Direction.D -> '┌'
                        i == moves && nextDirection == Direction.U -> '└'
                        else -> '─'
                    }

                    Direction.R -> when {
                        i == moves && nextDirection == Direction.D -> '┐'
                        i == moves && nextDirection == Direction.U -> '┘'
                        else -> '─'
                    }
                }
            }
        }

        // grid.joinToString("\n") { it.map { if (it) "#" else "." }.joinToString("") }.println()

        // Add all edges up to the path, so we can count intersections next
        val path = mutableListOf<Pair<Int, Int>>()
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] != '.') {
                    path.add(y to x)
                }
            }
        }

        val ignoredEdges = listOf('─', '└', '┘')
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
                        // Simulate we're shooting offset a bit, so we ignore -, L, J as intersections, otherwise we count edges too many times
                        val pipe = grid[y][x + offset]
                        if (pipe !in ignoredEdges) {
                            intersections++
                        }
                    }
                }
                if (intersections % 2 == 1) {
                    pointsInsideTheGrid += 1
                }
            }
        }
        return pointsInsideTheGrid.also { it.println() }
    }

    private data class Directions(val direction: Direction, val moves: Int)

    private fun parseInput(input: List<String>): List<Directions> {
        val lineData = "(.) (\\d+)".toRegex()
        return input.map { line ->
            val (direction, moves) = lineData.find(line)!!.destructured
            Directions(Direction.valueOf(direction), moves.toInt())
        }
    }

    private enum class Direction(val changeY: Int, val changeX: Int) {
        U(-1, 0),
        D(1, 0),
        L(0, -1),
        R(0, 1),
    }
}
