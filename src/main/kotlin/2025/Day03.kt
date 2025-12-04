package `2025`

import utils.println
import utils.readInput

fun main() {

    fun solve(input: List<String>, digits: Int): Long {
        return input.sumOf { batteries ->
            var options = batteries.toList()
            var digitsLeft = digits
            var result = ""
            while (digitsLeft > 0) {
                val highestNumber = options.dropLast(digitsLeft - 1).max()
                val index = options.indexOfFirst { it == highestNumber }
                options = options.drop(index + 1)
                result += highestNumber
                digitsLeft--
            }
            result.toLong()
        }
    }


    fun part1(input: List<String>): Long {
        return solve(input, 2)
    }

    val sampleInput = """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent()
    check(part1(sampleInput.lines()) == 357L)

    fun part2(input: List<String>): Long {
        return solve(input, 12)
    }

    val input = readInput("2025/Day03")
    part1(input).println()

    check(part2(sampleInput.lines()) == 3121910778619)

    part2(input).println()
}
