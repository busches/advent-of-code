package `2015`

import println
import readInput
import java.lang.IllegalArgumentException

fun main() {


    fun moveToNextHouse(c: Char, currentCoordinates: Pair<Int, Int>) = when (c) {
        '>' -> currentCoordinates.copy(first = currentCoordinates.first + 1)
        '<' -> currentCoordinates.copy(first = currentCoordinates.first - 1)
        '^' -> currentCoordinates.copy(second = currentCoordinates.second + 1)
        'v' -> currentCoordinates.copy(second = currentCoordinates.second - 1)
        else -> throw IllegalArgumentException(c.toString())
    }

    // Santa delivering
    fun part1(input: List<String>): Int {
        var currentCoordinates = Pair(0, 0)
        val housesVisited = mutableSetOf(currentCoordinates)
        input.first().forEach { c ->
            currentCoordinates =  moveToNextHouse(c, currentCoordinates)
            housesVisited.add(currentCoordinates)
        }

        return housesVisited.size
    }
    check(part1(listOf(">")) == 2)
    check(part1(listOf("^>v<")) == 4)
    check(part1(listOf("^v^v^v^v^v")) == 2)

    // Santa and Robot rotate delivering
    fun part2(input: List<String>): Int {
        var santaCoordinates = Pair(0, 0)
        var robotCoordinates = Pair(0, 0)
        val housesVisited = mutableSetOf(santaCoordinates)
        input.first().forEachIndexed { index, c ->
            val currentCoordinates = if (index % 2 == 0) santaCoordinates else robotCoordinates
            val updatedCoordinates = moveToNextHouse(c, currentCoordinates)
            housesVisited.add(updatedCoordinates)
            when {
                index % 2 == 0 -> santaCoordinates = updatedCoordinates
                else -> robotCoordinates = updatedCoordinates
            }
        }

        return housesVisited.size
    }

    check(part2(listOf("^v")) == 3)
    check(part2(listOf("^>v<")) == 3)
    check(part2(listOf("^v^v^v^v^v")) == 11)

    val input = readInput("2015/Day03")
    part1(input).println()
    part2(input).println()
}
