package `2016`

import utils.println
import utils.readInput
import utils.Directions.*
import kotlin.math.abs

fun main() {
    fun solve(input: List<String>, part1: Boolean): Int {
        val visitedPositions = emptySet<Pair<Int, Int>>().toMutableList().apply {
            0 to 0
        }
        var currentPosition = 0 to 0
        var facing = NORTH

        val allMoves = input[0].split(",").map { it.trim() }

        allMoves@ for (move in allMoves) {
            val turnDirection = move.substring(0, 1)
            val steps = move.substring(1).toInt()

            when (turnDirection) {
                "R" -> facing = when (facing) {
                    NORTH -> EAST
                    EAST -> SOUTH
                    SOUTH -> WEST
                    WEST -> NORTH
                }

                "L" -> facing = when (facing) {
                    NORTH -> WEST
                    EAST -> NORTH
                    SOUTH -> EAST
                    WEST -> SOUTH
                }

                else -> throw IllegalArgumentException(turnDirection)
            }

            for (step in 1..steps) {
                currentPosition = (currentPosition.first + (facing.x * 1)) to (currentPosition.second + (facing.y * 1))

                if (!part1) {
                    if (currentPosition in visitedPositions) {
                        "Revisited ${currentPosition}".println()
                        break@allMoves
                    } else {
                        visitedPositions.add(currentPosition)
                    }
                }
            }
        }

        "Ending Position: $currentPosition}".println()
        return abs(currentPosition.first) + abs(currentPosition.second)
    }

    fun part1(input: List<String>): Int {
        return solve(input, true)

    }

    check(part1(listOf("R2, L3")) == 5)
    check(part1(listOf("R2, R2, R2")) == 2)
    check(part1(listOf("R5, L5, R5, R3")) == 12)

    val input = readInput("2016/Day01_part1")
    part1(input).println()

    fun part2(input: List<String>): Int {
        return solve(input, false)
    }

    check(part2(listOf("R8, R4, R4, R8")) == 4)

    part2(input).println()
}
