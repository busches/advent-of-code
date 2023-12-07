package `2023`

import extractLongs
import println
import readInput
import utils.transpose

fun main() {

    // Time:      7  15   30
    //Distance:  9  40  200

    data class Race(val time: Long, val distance: Long)

    fun extractValues(input: List<String>) : List<Race> {
        return input.map { it.extractLongs().toList() }.transpose()
            .map { Race(it[0], it[1]) }
    }

    fun part1(input: List<String>): Int {
        return extractValues(input).map {(time, distance) ->
            (1..<time).filter { holdButtonLength ->
                val timeRemaining = time - holdButtonLength
                val distanceTraveled = timeRemaining * holdButtonLength
                distanceTraveled > distance
            }
                .count()
        }.fold(1) {acc, waysToWin ->
            acc * waysToWin
        }
    }

    val testInput = readInput("2023/Day06_Test")
    check(part1(testInput) == 288)

    val input = readInput("2023/Day06")
    part1(input).println()

    fun part2(input: List<String>): Int {
        TODO()
    }

    check(
        part2(
            listOf(
                "London to Dublin = 464",
                "London to Belfast = 518",
                "Dublin to Belfast = 141"
            )
        ) == 982
    )


    part2(input).println()
}
