package `2023`

import println
import readInput
import java.lang.IllegalArgumentException

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

fun main() {

    fun determineHandType(cards: String): HandType {
        val characterBuckets = cards.groupBy { it }.map { it.value.size }

        return when {
            characterBuckets.size == 1 -> HandType.FIVE_OF_A_KIND
            characterBuckets.max() == 4 -> HandType.FOUR_OF_A_KIND
            characterBuckets.size == 2 -> HandType.FULL_HOUSE
            characterBuckets.max() == 3 -> HandType.THREE_OF_A_KIND
            characterBuckets.size == 3 -> HandType.TWO_PAIR
            characterBuckets.size == 4 -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    data class Hand(
        val cards: String,
        val bid: Int,
        private val mapCardValue: (Char) -> String,
        val determineHandType: (String) -> HandType
    ) {
        val cardsForSorting: String
            get() = cards.map { card -> mapCardValue(card) }.joinToString("")
    }

    fun solve(
        input: List<String>,
        cardValue: (Char) -> String,
        handTypeKFunction1: (String) -> HandType
    ) = input
        .map {
            val (cards, bid) = it.split(" ")
            Hand(
                cards = cards,
                bid = bid.toInt(),
                mapCardValue = cardValue,
                determineHandType = handTypeKFunction1
            )
        }
        .sortedWith(compareBy({ it.determineHandType(it.cards) }, { it.cardsForSorting }))
        .reversed()
        .mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }
        .sum()

    fun part1(input: List<String>): Int {
        val cardValue = { card: Char ->
            when (card) {
                'A' -> "A"
                'K' -> "B"
                'Q' -> "C"
                'J' -> "D"
                'T' -> "E"
                '9' -> "F"
                '8' -> "G"
                '7' -> "H"
                '6' -> "I"
                '5' -> "J"
                '4' -> "K"
                '3' -> "L"
                '2' -> "M"
                else -> throw IllegalArgumentException("What is this $card")
            }
        }
        return solve(input, cardValue, ::determineHandType)
    }

    val testInput = readInput("2023/Day07_Test")
    check(part1(testInput) == 6440)

    val input = readInput("2023/Day07")
    part1(input).println()

    fun part2(input: List<String>): Int {
        val cardValue = { card: Char ->
            when (card) {
                'A' -> "A"
                'K' -> "B"
                'Q' -> "C"
                'J' -> "Z"
                'T' -> "E"
                '9' -> "F"
                '8' -> "G"
                '7' -> "H"
                '6' -> "I"
                '5' -> "J"
                '4' -> "K"
                '3' -> "L"
                '2' -> "M"
                else -> throw IllegalArgumentException("What is this $card")
            }
        }
        val determineHandType = { cards: String ->
            listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2")
                .map { card -> cards.replace("J", card) }
                .map { determineHandType(it) }
                .minOf { it }
        }
        return solve(input, cardValue, determineHandType)
    }

    check(part2(testInput) == 5905)
    part2(input).println()
}
