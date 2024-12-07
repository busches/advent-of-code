package `2024`

import println
import readInput

fun main() {
    val start = System.currentTimeMillis()

    val numberRegex = """\d+""".toRegex()
    fun solve(input: List<String>, operations: List<(Long, Long) -> Long>): Long {
        val equations = input.map { line ->
            val total = line.substringBefore(":").toLong()
            val numbers = numberRegex.findAll(line.substringAfter(":")).map {
                it.value.toLong()
            }.toList()
            total to numbers
        }

        // Loop over all equations, starting with the first number in the list
        // we then apply each operation to the running total
        return equations.mapNotNull { (total, numbers) ->
            val startingNumber = numbers.first()
            numbers
                .drop(1)
                .fold(listOf(startingNumber)) { runningTotals, nextNumber ->
                    runningTotals.flatMap { runningTotal ->
                        operations.mapNotNull { operation ->
                            operation(runningTotal, nextNumber).let {
                                // If we've already exceeded the total, no reason to keep trying new operations
                                if (it > total) null else it
                            }
                        }
                    }
                }
                .firstOrNull { it == total }
        }.sum()
    }

    val addition = { a: Long, b: Long -> a + b }
    val multiplication = { a: Long, b: Long -> a * b }
    val concatenation = { a: Long, b: Long -> (a.toString() + b.toString()).toLong() }

    fun part1(input: List<String>): Long {
        return solve(input, listOf(addition, multiplication))
    }

    fun part2(input: List<String>): Long {
        return solve(input, listOf(addition, multiplication, concatenation))
    }

    val sampleInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()
    check(part1(sampleInput) == 3749L)

    val input = readInput("2024/Day07")
    part1(input).println()

    check(part2(sampleInput) == 11387L)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
