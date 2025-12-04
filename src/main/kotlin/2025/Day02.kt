package `2025`

import utils.println
import utils.readInput
import kotlin.math.absoluteValue

fun main() {


    fun part1(input: List<String>): Long {
        val ranges = input.first().split(",")
        return ranges.sumOf { range ->
            "Range $range".println()
            val (startRange, endRange) = range.split("-")
            val invalidIds = (startRange.toLong()..endRange.toLong()).sumOf({ num ->
                var invalid = false
                val id = num.toString()
                if (id.length % 2 == 0) {
                    val midPoint = id.length / 2
                    val firstHalf = id.take(midPoint)
                    val secondHalf = id.substring(midPoint)
                    if (firstHalf == secondHalf) {
//                        "Invalid ID: $id".println()
                        invalid = true
                    }
                }
                if (invalid) num else 0
            })

            invalidIds
        }
    }

    val sampleInput = """
        11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
    """.trimIndent()
    check(part1(sampleInput.lines()) == 1227775554L)

    fun part2(input: List<String>): Int {
        TODO()
    }

    val input = readInput("2025/Day02")
    part1(input).println()

    check(part2(sampleInput.lines()) == 6)

    part2(input).println()
}
