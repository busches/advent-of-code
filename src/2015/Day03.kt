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

    fun visitHouses(moves: List<Char>) =
        moves.scan(Pair(0, 0)) { currentCoordinates, nextMove -> moveToNextHouse(nextMove, currentCoordinates) }.toSet()

    // Santa delivering
    fun part1(input: List<String>): Int {
        return visitHouses(input.first().toList()).size
    }
    check(part1(listOf(">")) == 2)
    check(part1(listOf("^>v<")) == 4)
    check(part1(listOf("^v^v^v^v^v")) == 2)

    // Santa and Robot rotate delivering
    fun part2(input: List<String>): Int {
        val (santa, robot) = input.first().withIndex().partition { it.index % 2 == 0 }
        val santaHouses = visitHouses(santa.map(IndexedValue<Char>::value))
        val robotHouses = visitHouses(robot.map(IndexedValue<Char>::value))

        return (santaHouses + robotHouses).size
    }

    check(part2(listOf("^v")) == 3)
    check(part2(listOf("^>v<")) == 3)
    check(part2(listOf("^v^v^v^v^v")) == 11)

    val input = readInput("2015/Day03")
    part1(input).println()
    part2(input).println()
}
