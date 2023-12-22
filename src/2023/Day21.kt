package `2023`

import println
import readInput
import kotlin.collections.ArrayDeque

fun main() {
    check(Day21().part1(readInput("2023/Day21_Test"), 6) == 16).also { "Check Part1 passed".println() }
    Day21().part1(readInput("2023/Day21")).println()

    check(Day21().part2(readInput("2023/Day21_Test2")) == 11687500L).also { "Check Part2 passed".println() }
    Day21().part2(readInput("2023/Day21")).println()
}

class Day21 {
    fun part1(input: List<String>, totalSteps: Int = 64): Int {
        val grid = input.map { it.toList() }

        val startY = grid.indexOfFirst { it.contains('S') }
        val startX = grid[startY].indexOf('S')

        val start = ElfWalker(startY, startX, Direction.UP, 0, totalSteps)

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


    fun part2(input: List<String>): Long {
        TODO()
    }

    private enum class Direction(val changeY: Int, val changeX: Int) {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1),
    }

    private data class ElfWalker(
        val y: Int,
        val x: Int,
        val direction: Direction,
        val numberOfSteps: Int,
        val totalSteps: Int,
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
                direction = direction,
                numberOfSteps = numberOfSteps + 1
            )
        }
    }
}
