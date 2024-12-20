package `2024`

import utils.println
import utils.readInput

fun main() {
    val start = System.currentTimeMillis()

    fun solve(input: List<String>): List<Long> {
        val towels = input.first().split(",").map { it.trim() }
        val patterns = input.takeLast(input.size - 2)

        val towelPatternCache = mutableMapOf<String, Long>()

        fun isTowelPattern(pattern: String): Long = towelPatternCache.getOrPut(pattern) {
            if (pattern.isEmpty()) {
                // We found all patterns
                1
            } else {
                towels
                    .filter { towel -> towel in pattern } // Only scan towels that are in the pattern
                    .sumOf { testTowel ->
                        if (pattern.startsWith(testTowel)) {
                            isTowelPattern(pattern.removePrefix(testTowel))
                        } else {
                            0
                        }
                    }

            }
        }

        return patterns.map { pattern -> isTowelPattern(pattern) }
    }

    fun part1(input: List<String>): Int {
        val matches = solve(input)
        return matches.count { it > 0 }
    }


    fun part2(input: List<String>): Long {
        val matches = solve(input)
        return matches.sum()
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

    check(part2(sampleInput) == 16L)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
