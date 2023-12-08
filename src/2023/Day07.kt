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

    fun handValue(cards: String): HandType {
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

    data class Hand(val cards: String, val bid: Int) : Comparable<Hand> {
        val handType: HandType
            get() = handValue(cards)

        override fun compareTo(other: Hand): Int {
            if (handType > other.handType) {
                return 1
            } else if (handType < other.handType) {
                return -1
            } else {
                cards.forEachIndexed { index, card ->
                    if (cardValue(card) > cardValue(other.cards[index])) {
                        return -1
                    } else if (cardValue(card) < cardValue(other.cards[index])) {
                        return 1
                    }
                }

                return 0
            }
        }

        private fun cardValue(card: Char): Int {
            return when (card) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 11
                'T' -> 10
                '9' -> 9
                '8' -> 8
                '7' -> 7
                '6' -> 6
                '5' -> 5
                '4' -> 4
                '3' -> 3
                '2' -> 2
                else -> throw IllegalArgumentException("What is this $card")
            }
        }
    }

    data class Hand2(val cards: String, val bid: Int) : Comparable<Hand2> {
        private val NON_WILDS = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2")

        val handType: HandType
            get() {
                // Map J to all the things
                if (cards.contains("J")) {
                    return NON_WILDS
                        .map { card -> cards.replace("J", card) }
                        .map { handValue(it) }
                        .minOf { it }
                } else {
                    return handValue(cards)
                }
            }

        override fun compareTo(other: Hand2): Int {
            if (handType > other.handType) {
                return 1
            } else if (handType < other.handType) {
                return -1
            } else {
                cards.forEachIndexed { index, card ->
                    if (cardValue(card) > cardValue(other.cards[index])) {
                        return -1
                    } else if (cardValue(card) < cardValue(other.cards[index])) {
                        return 1
                    }
                }

                return 0
            }
        }

        private fun cardValue(card: Char): Int {
            return when (card) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 1
                'T' -> 10
                '9' -> 9
                '8' -> 8
                '7' -> 7
                '6' -> 6
                '5' -> 5
                '4' -> 4
                '3' -> 3
                '2' -> 2
                else -> throw IllegalArgumentException("What is this $card")
            }
        }
    }

    fun part1(input: List<String>): Int {
        return input
            .map {
                val (cards, bid) = it.split(" ")
                Hand(cards, bid.toInt())
            }
            .sorted()
            .reversed()
            .mapIndexed { index, hand ->
                hand.bid * (index + 1)
            }
            .sum()
    }

    val testInput = readInput("2023/Day07_Test")
    check(part1(testInput) == 6440)

    val input = readInput("2023/Day07")
    part1(input).println()

    fun part2(input: List<String>): Int {
        return input
            .map {
                val (cards, bid) = it.split(" ")
                Hand2(cards, bid.toInt())
            }
            .sorted()
            .reversed()
            .mapIndexed { index, hand ->
                hand.bid * (index + 1)
            }
            .sum()
    }

    check(part2(testInput) == 5905)
    part2(input).println()
}
