package `2025`

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long {
        val findLineBreak = input.indexOf("")
        val freshIngredients = input.take(findLineBreak).map {
            val (startRange, endRange) = it.split("-")
            startRange.toLong()..endRange.toLong()
        }
        val ingredientsOnHand = input.drop(findLineBreak + 1)

        return ingredientsOnHand.count { ingredient ->
            freshIngredients.any { range -> (ingredient.toLong() in range) }
        }.toLong()
    }

    val sampleInput = """
        3-5
        10-14
        16-20
        12-18
        
        1
        5
        8
        11
        17
        32
    """.trimIndent()
    check(part1(sampleInput.lines()) == 3L)

    fun consolidate(freshIngredients: List<LongRange>): MutableList<LongRange> {
        val consolidatedRanges = mutableListOf<LongRange>()
        // Sorting is the kicker to merge efficiently
        for (range in freshIngredients.sortedBy { it.first }) {
            var newRange = range.first..range.last
            for (knownRange in consolidatedRanges) {
                if (newRange.first in knownRange) {
                    "${newRange.first} exists inside $knownRange, shrinking range to ${(knownRange.last + 1)..newRange.last}".println()
                    newRange = (knownRange.last + 1)..newRange.last
                }
                if (newRange.last in knownRange) {
                    "${newRange.last} exists inside $knownRange, shrinking range to ${newRange.first..<knownRange.first}".println()
                    newRange = newRange.first..<knownRange.first
                }
            }
            if (newRange.first <= newRange.last) {
                consolidatedRanges.add(newRange)
            }
        }
        return consolidatedRanges
    }

    fun part2(input: List<String>): Long {
        val findLineBreak = input.indexOf("")
        val freshIngredients = input.take(findLineBreak).map {
            val (startRange, endRange) = it.split("-")
            startRange.toLong()..endRange.toLong()
        }
        return consolidate(freshIngredients)
            .sumOf { range -> (range.last - range.first + 1).also { "$range has $it elements".println() } }
    }

    val input = readInput("2025/Day05")
    part1(input).println()

    check(part2(sampleInput.lines()) == 14L)

    part2(input).println()
}
