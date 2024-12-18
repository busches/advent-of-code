package `2024`

import utils.println
import utils.readInput
import kotlin.math.abs

fun main() {

    fun validReport(levels: List<Int>): Boolean {
        val groupedLevels = levels.zipWithNext()

        val allIncreasing = groupedLevels.all { (first, second) ->
            second > first
        }
        val allDecreasing = groupedLevels.all { (first, second) ->
            first > second
        }
        val allHaveDifferences = groupedLevels.all { (first, second) ->
            val differenceInLevels = abs(first - second)
            differenceInLevels in 1..3
        }
        return (allIncreasing || allDecreasing) && allHaveDifferences
    }

    fun part1(input: List<String>): Int {
        val parsedInput = input.map { it.split(" ").map { it.toInt() } }
        val safeReports = parsedInput.filter(::validReport)
        return safeReports.size
    }

    fun part2(input: List<String>): Int {
        val parsedInput = input.map { it.split(" ").map { it.toInt() } }
        val safeReports = parsedInput.filter { report ->

            val allPossibleReports = mutableListOf<List<Int>>()
            for (i in report.indices) {
                val newReport = report.toMutableList()
                newReport.removeAt(i)
                allPossibleReports.add(newReport)
            }
            allPossibleReports.any { validReport(it) }
        }
        return safeReports.size
    }

    val sampleInput = """
                7 6 4 2 1
                1 2 7 8 9
                9 7 6 2 1
                1 3 2 4 5
                8 6 4 4 1
                1 3 6 7 9
            """.trimIndent().split("\n")
    check(part1(sampleInput) == 2)

    val input = readInput("2024/Day02")
    part1(input).println()

    check(part2(sampleInput) == 4)

    part2(input).println()
}
