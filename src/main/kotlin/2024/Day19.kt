package `2024`

import utils.println
import utils.readInput

fun main() {
    val start = System.currentTimeMillis()

    fun part1(input: List<String>): Int {
        val towels = input.first().split(",").map { it.trim() }
        val patterns = input.takeLast(input.size - 2)

        val towelPatternCache = mutableMapOf<String, Boolean>()

        fun isTowelPattern(pattern: String): Boolean = towelPatternCache.getOrPut(pattern) {
            if (pattern.isEmpty()) {
                // We found all patterns
                true
            } else {
                towels
                    .filter { towel -> towel in pattern } // Only scan towels that are in the pattern
                    .any { testTowel ->
                        if (pattern.startsWith(testTowel)) {
                            isTowelPattern(pattern.removePrefix(testTowel))
                        } else {
                            false
                        }
                    }

            }
        }

        return patterns.map { pattern -> isTowelPattern(pattern) }.count { it }
    }


    fun part2(input: List<String>): Int {
        TODO("Need to implement Part 2")
    }

    val sampleInput = """
        r, wr, b, g, bwu, rb, gb, br
        
        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent().lines()
    check(part1(sampleInput) == 6)

    val input = readInput("2024/Day19")
    part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
