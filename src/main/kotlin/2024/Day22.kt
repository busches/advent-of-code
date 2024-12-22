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

        fun getAllSecretPricesFromSecret(startingSecret: Long): List<Int> {
            var secret = startingSecret
            val secretPrices = mutableListOf((startingSecret % 10).toInt())
            repeat(2000) {
                secret = generateNextSecret(secret)
                secretPrices.add((secret % 10).toInt())
            }
            return secretPrices
        }

        fun getIndexedPriceSequences(priceList: List<Int>): Map<List<Int>, Int> {
            val priceSequences = priceList.mapIndexedNotNull { index, price ->
                if (index < 4) {
                    null // Need to have at least 4 to do our manual window
                } else {
                    price to listOf(
                        priceList[index - 3] - priceList[index - 4],
                        priceList[index - 2] - priceList[index - 3],
                        priceList[index - 1] - priceList[index - 2],
                        priceList[index] - priceList[index - 1],
                    )
                }
            }

            return priceSequences.mapIndexedNotNull { index, (price, sequence) ->
                if (priceSequences.subList(0, index).any { it.second == sequence }) {
                    null // If we've seen this sequence already don't replace it, off by 2 error otherwise
                } else sequence to price
            }.toMap(LinkedHashMap())
        }

        fun part2(input: List<String>): Int {
            val allPrices = input.map { getAllSecretPricesFromSecret(it.toLong()) }
            val allPriceSequences = allPrices.map { getIndexedPriceSequences(it) }

            val uniqueSequences =
                allPriceSequences.flatMap { it.map { (priceSequence, _) -> priceSequence } }.toSet()

            return uniqueSequences
                .maxOf { sequence -> allPriceSequences.sumOf { it[sequence] ?: 0 } }
        }
    }
}

fun main() {
    val start = System.currentTimeMillis()

    val input = readInput("2024/Day22")
    Day22.part1(input).println()

    Day22.part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
