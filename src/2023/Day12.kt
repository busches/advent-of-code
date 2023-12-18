package `2023`

import println
import readInput


fun main() {
    check(Day12().part1(readInput("2023/Day12_Test")) == 21L).also { "Check Part1 passed".println() }
    Day12().part1(readInput("2023/Day12")).println()

    check(Day12().part2(readInput("2023/Day12_Test")) == 525152L).also { "Check Part2 passed".println() }
    Day12().part2(readInput("2023/Day12")).println()
}

class Day12 {

    fun part1(input: List<String>): Long {
        return input.sumOf { record ->
            val (springs, keys) = record.split(" ")
            val brokenGears = keys.split(',').map { it.toInt() }

            val combos = generateCombos(springs)
            countMatchingCombos(combos, brokenGears)
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { record ->
            val (springs, keys) = record.split(" ")
            val brokenGears = List(5) {keys}.joinToString(",").split(',').map { it.toInt() }

            val expandedSprings = List(5) {springs}.joinToString("?")
            val combos = generateCombos(expandedSprings)
            countMatchingCombos(combos, brokenGears)
        }
    }

    private fun countMatchingCombos(
        combos: List<String>,
        brokenGears: List<Int>
    ) = combos
        .map { combo ->
            val acc = mutableListOf(combo.first() to 0)
            combo.forEach { char ->
                val (prevChar, count) = acc.lastOrNull() ?: (char to 0)
                if (prevChar == char) {
                    acc[acc.size - 1] = char to count + 1
                } else {
                    acc.add(char to 1)
                }
            }
            acc.filter { (char, _) -> char == '#' }.map { (_, count) -> count }
        }.count { it == brokenGears }.toLong()

    private fun generateCombos(springs: String): List<String> {
        var combos = listOf(springs)
        while (combos.any { it.contains('?') }) {
            combos = combos.flatMap { startingCombo ->
                val firstQuestion = startingCombo.indexOf('?')
                if (firstQuestion >= 0) {
                    listOf(
                        StringBuilder(startingCombo).apply { setCharAt(firstQuestion, '.') }.toString(),
                        StringBuilder(startingCombo).apply { setCharAt(firstQuestion, '#') }.toString()
                    )
                } else {
                    emptyList()
                }
            }
        }
        return combos
    }
}
