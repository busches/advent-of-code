package `2024`

import utils.println
import utils.readInput

class Day22 {
    companion object {

        private fun Long.mix(newValue: Long) = xor(newValue)
        private fun Long.prune() = mod(16777216L)

        fun generateNextSecret(currentSecret: Long): Long {
            val step1 = currentSecret.mix(currentSecret * 64).prune()
            val step2 = step1.mix(step1.div(32)).prune()
            val step3 = step2.mix(step2 * 2048).prune()
            return step3
        }

        fun generateEntireDaysSecret(startingSecret: Long): Long {
            return (0..<2000).fold(startingSecret) { secret, _ -> generateNextSecret(secret) }
        }

        fun part1(input: List<String>): Long {
            return input.sumOf { generateEntireDaysSecret(it.toLong()) }
        }
    }
}

fun main() {
    val start = System.currentTimeMillis()

    fun part2(input: List<String>): Int {
        TODO("Need to implement Part 2")
    }

    val input = readInput("2024/Day22")
    Day22.part1(input).println()

//    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
