package `2015`

import println
import readInput
import kotlin.math.min

fun main() {
    fun surfaceAreaOfBox(l: Int, w: Int, h: Int): Int {
        val side1 = l * w
        val side2 = w * h
        val side3 = h * l
        val smallestSide = min(min(side1, side2), side3)
        return 2 * side1 + 2 * side2 + 2 * side3 + smallestSide // Smallest Side is needed for extra paper
    }

    fun ribbonNeeded(l: Int, w: Int, h: Int): Int {
        val dimensions = listOf(l, w, h).sorted()
        val ribbonForWrap = dimensions[0] * 2 + dimensions[1] * 2
        val ribbonForBow = l * w * h
        return ribbonForWrap + ribbonForBow
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { rawDimensions ->
            val dimensions = rawDimensions.split("x").map(Integer::valueOf)
            surfaceAreaOfBox(dimensions[0], dimensions[1], dimensions[2])
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { rawDimensions ->
            val dimensions = rawDimensions.split("x").map(Integer::valueOf)
            ribbonNeeded(dimensions[0], dimensions[1], dimensions[2])
        }
    }

    val input = readInput("2015/Day02")
    part1(input).println()
    part2(input).println()
}
