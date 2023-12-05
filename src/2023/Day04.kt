package `2023`

import println
import readInput
import kotlin.math.pow

fun main() {

    data class Card(val winningNumbers: List<Int>, val numbersYouHave: List<Int>) {
        val numberOfWinners = numbersYouHave.filter { it in winningNumbers }.size

        fun points(): Int {
            return when (numberOfWinners) {
                0 -> 0
                1 -> 1
                else -> 2.0.pow((numberOfWinners - 1).toDouble()).toInt()
            }
        }
    }

    fun extraValues(input: String): Card {
        val (_, winningNumbers, numbersYouHave) = input.split(":", "|")

        return Card(
            winningNumbers = winningNumbers.split(" ").filter { it.isNotBlank() }.map { it.toInt() },
            numbersYouHave = numbersYouHave.split(" ").filter { it.isNotBlank() }.map { it.toInt() },
        )
    }

    check(extraValues("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53").points() == 8)


    fun part1(input: List<String>): Int {
        return input.map { line -> extraValues(line) }.sumOf(Card::points)
    }

    check(
        part1(
            listOf(
                "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
            )
        ) == 13
    )

    val input = readInput("2023/Day04")
    part1(input).println()

    fun part2(input: List<String>): Int {
        val cards = input.map { line -> extraValues(line) }

        // If we reverse, we can calculate the cards value bottom up, instead of having to pass around cards
        return cards.reversed().fold(emptyList<Int>()) { acc, card ->
            val sum = 1 + (0..<(card.numberOfWinners)).sumOf { acc[it] }
            listOf(sum) + acc // Add new card winnings to the front of the list, since it's reversed
        }.sum()
    }

    check(
        part2(
            listOf(
                "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
            )
        ) == 30
    )

    part2(input).println()
}
