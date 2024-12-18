package `2024`

import utils.println
import utils.readInput

fun main() {
    val start = System.currentTimeMillis()

    fun solve(input: List<String>, blinks: Int): Long {
        val stones = input.first().split(" ").map { it.toLong() }

        val stoneMap = stones.associateWith { stone -> stones.count { it == stone }.toLong() }.toMutableMap()

        return (1..blinks).fold(stoneMap) { startingStoneMap, blink ->
            val updatedMap = mutableMapOf<Long, Long>()
            startingStoneMap.keys.toSet().forEach { stone ->
                val newStones = when {
                    stone == 0L -> listOf(1L)
                    stone.toString().length % 2 == 0 -> {
                        val stringStone = stone.toString()
                        val half = stringStone.length / 2
                        listOf(
                            stringStone.substring(0, half).toLong(), stringStone.substring(half).toLong()
                        )
                    }
                    else -> {
                        listOf(stone * 2024)
                    }
                }
                newStones.forEach { newStone ->
                    val currentCount = updatedMap.getOrDefault(newStone, 0L)
                    updatedMap[newStone] = currentCount + startingStoneMap[stone]!!
                }
            }
            updatedMap
        }.values.sum()
    }

    fun part1(input: List<String>): Long {
        return solve(input, 25)
    }


    fun part2(input: List<String>): Long {
        return solve(input, 75)
    }

    val sampleInput = """
        125 17
    """.trimIndent().lines()
    check(part1(sampleInput) == 55312L)

    val input = readInput("2024/Day11")
    part1(input).println()

    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
