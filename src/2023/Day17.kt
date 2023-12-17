package `2023`

import println
import readInput
import java.util.*


fun main() {
    check(Day17().part1(readInput("2023/Day17_Test")) == 102).also { "Check Part1 passed".println() }
    Day17().part1(readInput("2023/Day17")).println()

    check(Day17().part2(readInput("2023/Day17_Test")) == 94).also { "Check Part2 passed".println() }
    Day17().part2(readInput("2023/Day17")).println()
}


class Day17 {
    enum class Direction(val changeY: Int, val changeX: Int) {
        UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1),
    }

    data class Crucible(
        val y: Int,
        val x: Int,
        val direction: Direction,
        val directionDistance: Int,
    ) {
        fun nextMoves(): List<Direction> {
            val nextDirections = mutableListOf<Direction>()
            when (direction) {
                Direction.UP, Direction.DOWN -> {
                    nextDirections.add(Direction.LEFT)
                    nextDirections.add(Direction.RIGHT)
                }

                Direction.LEFT, Direction.RIGHT -> {
                    nextDirections.add(Direction.DOWN)
                    nextDirections.add(Direction.UP)
                }
            }

            if (directionDistance < 3) {
                nextDirections.add(direction)
            }
            return nextDirections
        }

        fun move(direction: Direction): Crucible {
            val newDistance = if (direction == this.direction) directionDistance + 1 else 1
            return Crucible(y + direction.changeY, x + direction.changeX, direction, newDistance)
        }
    }

    data class UltraCrucible(
        val y: Int,
        val x: Int,
        val direction: Direction,
        val directionDistance: Int,
    ) {
        fun nextMoves(): List<Direction> {
            val nextDirections = mutableListOf<Direction>()

            if (directionDistance > 3) {
                when (direction) {
                    Direction.UP, Direction.DOWN -> {
                        nextDirections.add(Direction.LEFT)
                        nextDirections.add(Direction.RIGHT)
                    }

                    Direction.LEFT, Direction.RIGHT -> {
                        nextDirections.add(Direction.DOWN)
                        nextDirections.add(Direction.UP)
                    }
                }
            }

            if (directionDistance < 10) {
                nextDirections.add(direction)
            }


            return nextDirections
        }

        fun move(direction: Direction): UltraCrucible {
            val newDistance = if (direction == this.direction) directionDistance + 1 else 1
            return UltraCrucible(y + direction.changeY, x + direction.changeX, direction, newDistance)
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }

        val start = Crucible(0, 0, Direction.RIGHT, 0)
        // Use Priority Queue, not a LinkedList, so we can visit the lowest cost values and cache them first, otherwise
        // We will cache non-optimal paths and never visit them again :)
        val queue =
            PriorityQueue<IndexedValue<Crucible>>(compareBy { (cost, crucible) -> cost - crucible.x - crucible.y })
        val visited = mutableSetOf<Crucible>()
        queue.add(IndexedValue(0, start))

        while (queue.isNotEmpty()) {
            val (cost, crucible) = queue.remove()

            if (!visited.add(crucible)) {
                continue // Already been here
            }

            if (crucible.x == grid.first().lastIndex && crucible.y == grid.lastIndex) {
                // We can return here, as we're using the priority queue based on cost and the first exit, should be cheapest
                return cost
            }

            for (direction in crucible.nextMoves()) {
                val newCrucible = crucible.move(direction)
                // Bounds check
                if (newCrucible.x !in grid.first().indices || newCrucible.y !in grid.indices) {
                    continue
                }
                val costOfSpace = grid[newCrucible.y][newCrucible.x].digitToInt()
                val newCost = cost + costOfSpace
                queue.add(IndexedValue(newCost, newCrucible))
            }
        }

        TODO("Didn't finish?")
    }

    fun part2(input: List<String>): Int {
        // Duplication FTW
        val grid = input.map { it.toList() }

        val start = UltraCrucible(0, 0, Direction.RIGHT, 0)
        // Use Priority Queue, not a LinkedList, so we can visit the lowest cost values and cache them first, otherwise
        // We will cache non-optimal paths and never visit them again :)
        val queue =
            PriorityQueue<IndexedValue<UltraCrucible>>(compareBy { (cost, crucible) -> cost - crucible.x - crucible.y })
        val visited = mutableSetOf<UltraCrucible>()
        queue.add(IndexedValue(0, start))

        while (queue.isNotEmpty()) {
            val (cost, crucible) = queue.remove()

            if (!visited.add(crucible)) {
                continue // Already been here
            }

            if (crucible.x == grid.first().lastIndex && crucible.y == grid.lastIndex) {
                // We can return here, as we're using the priority queue based on cost and the first exit, should be cheapest
                return cost
            }

            for (direction in crucible.nextMoves()) {
                val newCrucible = crucible.move(direction)
                // Bounds check
                if (newCrucible.x !in grid.first().indices || newCrucible.y !in grid.indices) {
                    continue
                }
                val costOfSpace = grid[newCrucible.y][newCrucible.x].digitToInt()
                val newCost = cost + costOfSpace
                queue.add(IndexedValue(newCost, newCrucible))
            }
        }

        TODO("Didn't finish?")
    }


}
