package `2025`

import utils.println
import utils.readInput
import utils.transpose

fun main() {
    fun part1(input: List<String>): Long {
        val rows = input.map { line -> line.split("\\s+".toRegex()).filter { it.isNotBlank() } }.transpose()

        return rows.sumOf { row ->
            val operator = row.last()
            val operation = if (operator == "*")
                { a: Long, b: Long -> a * b } else { a: Long, b: Long -> a + b }
            val numbers = row.dropLast(1).map { it.toLong() }

            val initial = if (operator == "*") 1L else 0L
            numbers.fold(initial, { acc, value -> operation(acc, value) })
        }
    }


    val sampleInput = """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +  
    """.trimIndent()
    check(part1(sampleInput.lines()) == 4277556L)


    fun part2(input: List<String>): Long {
        TODO()
    }

    val input = readInput("2025/Day06")
    part1(input).println()

    check(part2(sampleInput.lines()) == 3263827L)

    part2(input).println()
}
