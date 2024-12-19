package `2024`

import utils.Directions
import utils.println
import utils.readInput
import java.util.*

fun main() {
    val start = System.currentTimeMillis()

    data class Coordinate(val x: Int, val y: Int) {
        operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
    }

    data class Move(val position: Coordinate, val steps: Int) {}

    fun solve(gridSize: Int, fallenBytes: List<Coordinate>): Int {
        val visited = mutableSetOf<Coordinate>()
        val queue = PriorityQueue<Move>(compareBy { it.steps })
        queue += Move(Coordinate(0, 0), 0)

        while (queue.isNotEmpty()) {
            val move = queue.remove()

            if (!visited.add(move.position)) {
                // been here
                continue
            }

            if (move.position.x == gridSize && move.position.y == gridSize) {
                return move.steps
            }

            val newMoves = Directions.entries.mapNotNull { (x, y) ->
                val nextCoordinate = move.position + Coordinate(x, y)
                if (nextCoordinate.x > gridSize || nextCoordinate.y > gridSize ||
                    nextCoordinate.x < 0 || nextCoordinate.y < 0
                ) {
                    // can't walk off the grid
                    null
                } else if (nextCoordinate in fallenBytes) {
                    // can't move into a byte
                    null
                } else {
                    move.copy(position = nextCoordinate, steps = move.steps + 1)
                }
            }
            queue += newMoves
        }
        return -1
    }

    fun part1(input: List<String>, gridSize: Int = 70, numberOfFallenBytes: Int = 1024): Int {
        val fallenBytes = input
            .take(numberOfFallenBytes)
            .map { it.split(",") }
            .map { (x, y) -> Coordinate(x.toInt(), y.toInt()) }

        return solve(gridSize, fallenBytes)
    }


    fun part2(input: List<String>, gridSize: Int = 70): String {
        val allFallenBytes = input.map { it.split(",") }.map { (x, y) -> Coordinate(x.toInt(), y.toInt()) }

        var totalFallenBytes = 1
        while (totalFallenBytes != allFallenBytes.size) {
            val fallenBytes = allFallenBytes.take(totalFallenBytes)
            if (solve(gridSize, fallenBytes) == -1) {
                return input[totalFallenBytes - 1]
            }
            totalFallenBytes++
        }
        TODO("Tried them all?")
    }

    val sampleInput = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
    """.trimIndent().lines()
    check(part1(sampleInput, 6, 12) == 22)


    val input = readInput("2024/Day18")
    part1(input).println()

    check(part2(sampleInput, 6) == "6,1")
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
