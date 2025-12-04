package `2025`

import utils.println
import utils.readInput
import kotlin.math.absoluteValue

fun main() {

    fun solve(input: List<String>, part2: Boolean): Int {
        var dialPosition = 50
        var timesAtZero = 0
        var pastZero = 0

        var previousPosition = 50
        input.forEach { instruction ->
            val direction = instruction.first()
            val turn = instruction.substring(1).toInt()

            val numberToTurn = when (direction) {
                'L' -> turn * -1
                'R' -> turn
                else -> throw IllegalArgumentException("Unexpected direction of $direction")
            }

            dialPosition += numberToTurn % 100
            pastZero += (numberToTurn / 100).absoluteValue
            if (dialPosition >= 100) {
                dialPosition -= 100
                if (dialPosition !=0) {
                    pastZero++
                }
            } else if (dialPosition < 0) {
                dialPosition += 100
                if (previousPosition != 0 && dialPosition !=0) {
                    pastZero++
                }
            }
            if (dialPosition == 0) {
                timesAtZero++
            }

            previousPosition = dialPosition
        }

        return if (part2) pastZero + timesAtZero else timesAtZero
    }

    fun part1(input: List<String>): Int {
        return solve(input, false)
    }

    val sampleInput = """
        L68
        L30
        R48
        L5
        R60
        L55
        L1
        L99
        R14
        L82
    """.trimIndent()
    check(part1(sampleInput.lines()) == 3)

    fun part2(input: List<String>): Int {
        return solve(input, true)
    }

    val input = readInput("2025/Day01")
    part1(input).println()

    check(part2(sampleInput.lines()) == 6)

    part2(input).println()
}
