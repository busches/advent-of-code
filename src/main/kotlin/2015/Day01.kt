package `2015`

import utils.println
import utils.readInput

fun main() {
    fun upOrDown(c: Char) = if (c == '(') 1 else -1

    fun part1(input: List<String>): Int {
        val floors = input.first().fold(0) { acc, c ->
            acc + upOrDown(c)
        }

        return floors
    }

    fun part2(input: List<String>): Int {
        var floor = 0
        for ((index, c) in input.first().withIndex()) {
            floor += upOrDown(c)
            if (floor == -1) {
                return index + 1
            }
        }
        return -1
    }

    val input = readInput("2015/Day01")
    part1(input).println()
    part2(input).println()
}
