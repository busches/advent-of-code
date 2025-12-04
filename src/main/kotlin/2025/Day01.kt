package `2025`

import utils.println
import utils.readInput

fun main() {

    fun part1(input: List<String>): Int {
        var dialPosition = 50
        var timesAtZero = 0

        input.forEach { instruction ->
            val direction = instruction.first()
            val turn = instruction.substring(1).toInt()

            val numberToTurn = when (direction) {
                'L' -> turn * -1
                'R' -> turn
                else -> throw IllegalArgumentException("Unexpected direction of $direction")
            }

            dialPosition += numberToTurn % 100
            if (dialPosition >= 100) {
                dialPosition -= 100
            } else if (dialPosition < 0) {
                dialPosition += 100
            }
            if (dialPosition == 0) {
                timesAtZero++
            }
//            println("Rotated $instruction now at $dialPosition")
        }

//        println(timesAtZero)
        return timesAtZero
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
        TODO()
    }

    val input = readInput("2025/Day01")
    part1(input).println()

    check(part2(sampleInput.lines()) == 31)

    part2(input).println()
}
