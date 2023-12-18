package `2023`

import println
import readInput


fun main() {
    check(Day12().part1(readInput("2023/Day12_Test")) == 21).also { "Check Part1 passed".println() }
    Day12().part1(readInput("2023/Day12")).println()

    check(Day12().part2(readInput("2023/Day12_Test")) == 94).also { "Check Part2 passed".println() }
    Day12().part2(readInput("2023/Day12")).println()
}


class Day12 {

    fun part1(input: List<String>): Int {
        return input.sumOf { record ->
            val (springs, keys) = record.split(" ")
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

            val brokenGears = keys.split(',').map { it.toInt() }

            combos.asSequence()
                .map { combo ->
                    combo.chunked(1).fold(emptyList<Pair<String, Int>>()) { acc, char ->
                        val (prevChar, count) = acc.lastOrNull() ?: (char to 0)
                        if (prevChar == char) {
                            acc.dropLast(1) + (char to count + 1)
                        } else {
                            acc + (char to 1)
                        }
                    }
                }
                .map { it.filter { (char, _) -> char == "#" }.map { (_, count) -> count } }
                .filter { it == brokenGears }
                .count()
        }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }
}
