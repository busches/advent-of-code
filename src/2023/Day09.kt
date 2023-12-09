package `2023`

import extractLongs
import println
import readInput
import kotlin.math.absoluteValue

fun main() {

    fun extractSequences(input: List<String>): List<List<Long>> {
        return input.map { it.extractLongs().toList() }
    }


    fun part1(input: List<String>): Long {
        return extractSequences(input)
            .map { startingSequence ->
                val allSequences = mutableListOf(startingSequence)
                while (!allSequences.last().all { it == 0L }) {
                    val nextSequence = allSequences.last().zipWithNext()
                        .map { (first, second) -> (second - first) }

                    allSequences.add(nextSequence)
                }

                allSequences.reversed()
                    .map { it.last() }.fold(0L, Long::plus)
            }
            .sum()
    }

    check(part1(readInput("2023/Day09_Test")) == 114L)

    val input = readInput("2023/Day09")
    part1(input).println()


    fun part2(input: List<String>): Long {
        TODO()
    }

    check(part2(readInput("2023/Day09_Test2")) == 6L)
    part2(input).println()
}
