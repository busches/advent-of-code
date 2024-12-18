package `2023`

import utils.println
import utils.readInput
import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    check(Day21().part1(readInput("2023/Day21_Test"), 6) == 16).also { "Check Part1 passed".println() }
    Day21().part1(readInput("2023/Day21")).println()

    check(Day21().part2(readInput("2023/Day21_Test"), 6) == 16).also { "Check Part2-1 passed".println() }
    check(Day21().part2(readInput("2023/Day21_Test"), 10) == 50).also { "Check Part2-2 passed".println() }
    check(Day21().part2(readInput("2023/Day21_Test"), 50) == 1594).also { "Check Part2-3 passed".println() }
    check(Day21().part2(readInput("2023/Day21_Test"), 100) == 6536).also { "Check Part2-4 passed".println() }
    check(Day21().part2(readInput("2023/Day21_Test"), 500) == 167004).also { "Check Part2-5 passed".println() }
    check(Day21().part2(readInput("2023/Day21_Test"), 1000) == 668697).also { "Check Part2-6 passed".println() }
    check(Day21().part2(readInput("2023/Day21_Test"), 5000) == 16733044).also { "Check Part2-7 passed".println() }
    Day21().part2(readInput("2023/Day21"), 26_501_365).println()
}

class Day21 {
    fun part1(input: List<String>, totalSteps: Int = 64): Int {
        val grid = input.map { it.toList() }

        val startY = grid.indexOfFirst { it.contains('S') }
        val startX = grid[startY].indexOf('S')

        val start = ElfWalker(startY, startX, 0, totalSteps)

        val queue = ArrayDeque<ElfWalker>()
        val visited = mutableSetOf<ElfWalker>()
        val endingSpots = mutableSetOf<ElfWalker>()
        queue += start

        while (queue.isNotEmpty()) {
            val elfWalker = queue.removeFirst()

            if (!visited.add(elfWalker)) {
                continue // Already been here
            }

            if (elfWalker.nextMoves().isEmpty()) {
                endingSpots.add(elfWalker)
            }

            for (direction in elfWalker.nextMoves()) {
                val newWalker = elfWalker.move(direction)
                // Bounds check
                if (newWalker.x !in grid.first().indices || newWalker.y !in grid.indices) {
                    continue
                }
                val spaceType = grid[newWalker.y][newWalker.x]
                if (spaceType == '.' || spaceType == 'S')
                    queue.add(newWalker)
            }
        }

        val visitedCoords = endingSpots.map { it.y to it.x }.toSet()
        grid.mapIndexed { indexY, line ->
            line.mapIndexed { indexX, c ->
                if (visitedCoords.contains(indexY to indexX)) {
                    'O'
                } else c
            }
        }.joinToString("\n") { it.joinToString("") }.println()

        return endingSpots.map { it.y to it.x }.toSet().size.also { it.println() }
    }


    fun part2(input: List<String>, totalSteps: Int): Int {
        val grid = input.map { it.toList() }

        val startY = grid.indexOfFirst { it.contains('S') }
        val startX = grid[startY].indexOf('S')

        val start = ElfWalker(startY, startX, 0, totalSteps)

        val queue = PriorityQueue<ElfWalker>(compareBy { elf -> elf.numberOfSteps })
        val visited = mutableSetOf<ElfWalker>()
        val endingSpots = mutableSetOf<ElfWalker>()
        queue += start

        val maxX = grid.first().indices.last
        val maxY = grid.indices.last

        while (queue.isNotEmpty()) {
            val elfWalker = queue.remove()

            if (!visited.add(elfWalker)) {
                continue // Already been here
            }

            if (elfWalker.nextMoves().isEmpty()) {
                val xMultiplier = elfWalker.crossedX * (maxX + 1)
                val newX = if (xMultiplier >= 0) elfWalker.x + xMultiplier else elfWalker.x * -1 + xMultiplier
                val yMultiplier = elfWalker.crossedY * (maxY + 1)
                val newY = if (yMultiplier >= 0) elfWalker.y + yMultiplier else elfWalker.y * -1 + yMultiplier
                val updatedWalker = elfWalker.copy(x = newX, y = newY)
                endingSpots.add(updatedWalker)
            }

            for (direction in elfWalker.nextMoves()) {
                var newWalker = elfWalker.move(direction)
                // Bounds check is a lie, we are infinite!
                if (newWalker.x < 0) {
                    newWalker = newWalker.copy(x = maxX, crossedX = newWalker.crossedX - 1)
                } else if (newWalker.y < 0) {
                    newWalker = newWalker.copy(y = maxY, crossedY = newWalker.crossedY - 1)
                } else if (newWalker.x > maxX) {
                    newWalker = newWalker.copy(x = 0, crossedX = newWalker.crossedX + 1)
                } else if (newWalker.y > maxY) {
                    newWalker = newWalker.copy(y = 0, crossedY = newWalker.crossedY + 1)
                }

                val spaceType = grid[newWalker.y][newWalker.x]
                if (spaceType == '.' || spaceType == 'S') {
                    queue.add(newWalker)
                }
            }
        }

        return endingSpots.map { it.y to it.x }.toSet().size.also { it.println() }
    }

    private enum class Direction(val changeY: Int, val changeX: Int) {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1),
    }

    private data class ElfWalker(
        val y: Int,
        val x: Int,
        val numberOfSteps: Int,
        val totalSteps: Int,
        val crossedX: Int = 0,
        val crossedY: Int = 0,
    ) {
        fun nextMoves(): List<Direction> {
            return if (numberOfSteps < totalSteps) {
                listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
            } else {
                emptyList()
            }
        }

        fun move(direction: Direction): ElfWalker {
            return copy(
                y = y + direction.changeY,
                x = x + direction.changeX,
                numberOfSteps = numberOfSteps + 1
            )
        }
    }
}
