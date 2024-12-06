package `2024`

import println
import readInput
import java.util.*

fun main() {

    fun parseInput(input: List<String>): Pair<MutableMap<Int, MutableSet<Int>>, MutableList<List<Int>>> {
        return input.fold(mutableMapOf<Int, MutableSet<Int>>() to mutableListOf()) { (pageOrderingRules, updates), line ->

            if (line.contains("|")) {
                val (page1, page2) = line.split("|")
                val rules = pageOrderingRules.getOrDefault(page1.toInt(), mutableSetOf())
                rules.add(page2.toInt())
                pageOrderingRules[page1.toInt()] = rules
            } else if (line.contains(",")) {
                updates.add(line.split(",").map(String::toInt))
            }

            pageOrderingRules to updates
        }
    }

    fun isOrderedCorrectly(
        update: List<Int>,
        pageOrderingRules: MutableMap<Int, MutableSet<Int>>
    ): Boolean {
        for ((index, page) in update.withIndex()) {
            val pagesThatMustComeFirst = pageOrderingRules.getOrDefault(page, emptySet())
            val pagesBeforeThis = update.take(index)
            if (pagesThatMustComeFirst.any { pagesBeforeThis.contains(it) }) {
                return false
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val (pageOrderingRules, updates) = parseInput(input)

        val correctlyOrderedUpdates = updates.filter { update ->
            isOrderedCorrectly(update, pageOrderingRules)
        }

        val middlePages = correctlyOrderedUpdates.map { update ->
            update[update.size / 2]
        }

        return middlePages.sum()
    }


    fun part2(input: List<String>): Int {
        val (pageOrderingRules, updates) = parseInput(input)

        val incorrectlyOrderedUpdates = updates.filter { update ->
            !isOrderedCorrectly(update, pageOrderingRules)
        }

        val correctlyOrderedUpdates = incorrectlyOrderedUpdates.map { update ->

            val allPages = update.toMutableList()
            val correctlyOrderedPages = mutableListOf<Int>()
            var swapOffset = 1

            while (allPages.isNotEmpty()) {
                val index = allPages.size - 1
                val page = allPages.last()
                val pagesThatMustComeFirst = pageOrderingRules.getOrDefault(page, emptySet())
                val pagesBeforeThis = allPages.take(index)
                if (pagesThatMustComeFirst.any { pagesBeforeThis.contains(it) }) {
                    // Swap some pages
                    Collections.swap(allPages, index, index - swapOffset)
                    "Swapped ${allPages[index]} and ${allPages[index-swapOffset]}".println()
                    swapOffset++
                } else {
                    "$page is correct".println()
                    correctlyOrderedPages.add(0, page)
                    allPages.removeLast()
                    swapOffset = 1
                }
            }

            correctlyOrderedPages
        }


        val middlePages = correctlyOrderedUpdates.map { update ->
            update[update.size / 2]
        }

        return middlePages.sum()
    }

    val sampleInput = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().lines()
    check(part1(sampleInput) == 143)

    val input = readInput("2024/Day05")
    part1(input).println()

    check(part2(sampleInput) == 123)
    part2(input).println()
}
