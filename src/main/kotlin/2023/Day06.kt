package `2023`

import extractLongs
import utils.println
import utils.readInput
import utils.transpose

fun main() {
    data class Race(val time: Long, val distance: Long)

    fun extractValuesPart1(input: List<String>): List<Race> {
        return input.map { it.extractLongs().toList() }.transpose()
            .map { Race(it[0], it[1]) }
    }

    fun waysToWinMultiplied(blah: List<Race>) = blah
        .map { (time, distance) ->
            (1..<time).count { holdButtonLength ->
                val timeRemaining = time - holdButtonLength
                val distanceTraveled = timeRemaining * holdButtonLength
                distanceTraveled > distance
            }
        }.fold(1) { acc, waysToWin ->
            acc * waysToWin
        }

    fun part1(input: List<String>): Int {
        return waysToWinMultiplied(extractValuesPart1(input))
    }

    val testInput = readInput("2023/Day06_Test")
    check(part1(testInput) == 288)

    val input = readInput("2023/Day06")
    part1(input).println()

    fun extractValuesPart2(input: List<String>): List<Race> {
        val rawRaceValues = input.map { it.extractLongs().toList().fold("") { acc, num -> acc + num } }
            .map { it.toLong() }

        return listOf(Race(rawRaceValues[0], rawRaceValues[1]))
    }


    fun part2(input: List<String>): Int {
        return waysToWinMultiplied(extractValuesPart2(input))
    }

    check(part2(testInput) == 71503)
    part2(input).println()
}
