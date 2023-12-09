package `2023`

import extractLongs
import println
import readInput

fun main() {

    fun extractSequences(input: List<String>): List<List<Long>> {
        return input.map { it.extractLongs().toList() }
    }

    fun part1(input: List<String>): Long {
        return extractSequences(input).sumOf { startingSequence ->
            generateSequence(startingSequence) { sequence ->
                sequence.zipWithNext().map { (first, second) -> (second - first) }
            }
                .takeWhile { sequence -> sequence.any { it != 0L } }
                .map { it.last() }
                .fold(0L, Long::plus)
        }
    }

    check(part1(readInput("2023/Day09_Test")) == 114L)

    val input = readInput("2023/Day09")
    part1(input).println()


    fun part2(input: List<String>): Long {
        return extractSequences(input).map { it.reversed() }.sumOf { startingSequence ->
            val allSequences = generateSequence(startingSequence) { sequence ->
                sequence.zipWithNext().map { (first, second) -> (first - second) }
            }.takeWhile { sequence -> sequence.any { it != 0L } }


            val finalNumbers = allSequences.toList().reversed().map { it.last() }
            val newNumbers = mutableListOf(0L)

            finalNumbers.map {
                val nextNumber = it - newNumbers.last()
                newNumbers.add(nextNumber)
                nextNumber
            }

            newNumbers.last()
        }
    }

    check(part2(readInput("2023/Day09_Test")) == 2L)
    part2(input).println()
}
