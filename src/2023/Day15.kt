package `2023`

import println
import readInput

fun main() {

    fun hashAlgo(input: String): Long {
        return input
            .map { it.code }
            .fold(0L) { acc, letter ->
                (acc + letter) * 17 % 256
            }
            .also { "$input -> $it".println() }
    }

    check(hashAlgo("HASH") == 52L)

    fun part1(input: List<String>): Long {
        return input.first().split(",").sumOf { hashAlgo(it) }
    }

    check(part1(readInput("2023/Day15_Test")) == 1320L)

    val input = readInput("2023/Day15")
    part1(input).println()

    fun part2(input: List<String>): Long {
        TODO()
    }

    part2(input).println()
}
