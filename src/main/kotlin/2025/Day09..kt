package `2025`

import utils.combinations
import utils.println
import utils.readInput
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Long {
        val coordinates = input.map {
            val (x, y) = it.split(",")
            Coordinate(x.toInt(), y.toInt())
        }
        return coordinates.combinations().maxOf { (coordinate1, coordinate2) -> 
            val length = (coordinate1.x - coordinate2.x).absoluteValue.toLong() + 1
            val width = (coordinate1.y - coordinate2.y).absoluteValue + 1
            length * width
        }
    }

    val sampleInput = """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent()
    check(part1(sampleInput.lines()) == 50L)

    fun part2(input: List<String>): Long {
        TODO()
    }


    val input = readInput("2025/Day09")
    part1(input).println()

    check(part2(sampleInput.lines()) == 25272L)

    part2(input).println()
}
