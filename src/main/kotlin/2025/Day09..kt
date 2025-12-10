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
            // +1 as it's inclusive, we get to count the whole square
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
    
    data class Side(val start: Coordinate, val end: Coordinate) {
        fun contains(coordinate: Coordinate): Boolean {
            return coordinate.x in start.x..end.y && coordinate.y in start.y..end.y
        }
    }

    fun part2(input: List<String>): Long {
        val coordinates = input.map {
            val (x, y) = it.split(",")
            Coordinate(x.toInt(), y.toInt())
        }
        // Make all of them into sides, they appear to be ordered?
        // Need to add the first one again to complete the loop
        val sides = (coordinates + coordinates.first())
            .zipWithNext()
            .map { (coordinate1, coordinate2) -> Side(coordinate1, coordinate2) }


        return coordinates.combinations().maxOf { (coordinate1, coordinate2) ->
            // Confirm the rectangle we make from these coordinates
            // exists inside the rectangle formed by all the edges
            // TODO figure out how to make that rectangle
            
            val length = (coordinate1.x - coordinate2.x).absoluteValue.toLong() + 1
            val width = (coordinate1.y - coordinate2.y).absoluteValue + 1
            length * width
        }
        
        
        
        TODO()
    }


    val input = readInput("2025/Day09")
    part1(input).println()

    check(part2(sampleInput.lines()) == 15L)

    part2(input).println()
}
