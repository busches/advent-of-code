package `2024`

import utils.println
import utils.readInput

class Day21 {
    enum class Key {
        A, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
    }


    companion object {
        fun getRobotOneKeyPresses(keysToPress: String): List<String> {
            val startingKey = Key.A
            var remainingKeysToPress = keysToPress.toList().map { it.toKey() }




            TODO("Not yet implemented")
        }
    }
}

private fun Char.toKey(): Day21.Key {
    return when (this) {
        'A' -> Day21.Key.A
        '0' -> Day21.Key.ZERO
        '1' -> Day21.Key.ONE
        '2' -> Day21.Key.TWO
        '3' -> Day21.Key.THREE
        '4' -> Day21.Key.FOUR
        '5' -> Day21.Key.FIVE
        '6' -> Day21.Key.SIX
        '7' -> Day21.Key.SEVEN
        '8' -> Day21.Key.EIGHT
        '9' -> Day21.Key.NINE
        else -> throw IllegalStateException("Unexpected character $this")
    }
}

fun main() {
    val start = System.currentTimeMillis()

    fun part1(input: List<String>): Int {
        TODO("Need to implement Part 1")
    }


    fun part2(input: List<String>): Int {
        TODO("Need to implement Part 2")
    }

    val sampleInput = """
        029A
        980A
        179A
        456A
        379A
    """.trimIndent().lines()
    check(part1(sampleInput) == 126384)

    val input = readInput("2024/Day21")
    part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
