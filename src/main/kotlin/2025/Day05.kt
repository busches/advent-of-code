package `2025`

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Long {
        val findLineBreak = input.indexOf("")
        val freshIngredients = input.take(findLineBreak).map {
            val (startRange, endRange) = it.split("-")
            startRange.toLong()..endRange.toLong()
        }
        val ingredientsOnHand = input.drop(findLineBreak + 1);
        ingredientsOnHand.println()

        return ingredientsOnHand.count { ingredient ->
            freshIngredients.any { range ->
                (ingredient.toLong() in range)
//                    .also { "$ingredient in $range - $it".println() }
            }
//                .also { "$ingredient is ${if (it) "fresh" else "spoiled"}".println() }
        }.toLong()
    }

    val sampleInput = """
        3-5
        10-14
        16-20
        12-18
        
        1
        5
        8
        11
        17
        32
    """.trimIndent()
    check(part1(sampleInput.lines()) == 3L)

    fun part2(input: List<String>): Long {
        TODO()
    }

    val input = readInput("2025/Day05")
    part1(input).println()

    check(part2(sampleInput.lines()) == 43L)

    part2(input).println()
}
