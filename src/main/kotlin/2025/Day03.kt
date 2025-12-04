package `2025`

import utils.println
import utils.readInput
import kotlin.math.absoluteValue

fun main() {


    fun part1(input: List<String>): Long {
        return input.sumOf { batteries ->
            val options = batteries.toList()
            val highestNumber = options.dropLast(1).max()
            val index = options.indexOfFirst { it == highestNumber }
            val secondHighest = options.drop(index + 1).max()

            "$highestNumber$secondHighest".toLong()
        }
    }

    val sampleInput = """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent()
    check(part1(sampleInput.lines()) == 357L)

    fun part2(input: List<String>): Long {
       TODO()
    }

    val input = readInput("2025/Day03")
    part1(input).println()

    check(part2(sampleInput.lines()) == 4174379265L)

    part2(input).println()
}
