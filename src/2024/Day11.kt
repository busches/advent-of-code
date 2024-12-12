package `2024`

import println
import readInput

fun main() {
    val start = System.currentTimeMillis()

    fun solve(input: List<String>, blinks: Int): Int {
        val stones = input.first().split(" ").map { it.toLong() }



        val queue = ArrayDeque(stones.map { 0 to it })

        var finishedStones = 0
        while (queue.isNotEmpty()) {
            // Probably need to cache the blinks and stone, as we'll recalculate some paths again and again
            val (blink, stone) = queue.removeFirst()
            if (blink == blinks) {
                finishedStones++
            } else {
                when {
                    stone == 0L -> queue.addFirst(blink + 1 to 1L)
                    stone.toString().length % 2 == 0 -> {
                        val stringStone = stone.toString()
                        val half = stringStone.length / 2
                        queue.addFirst(blink + 1 to stringStone.substring(0, half).toLong())
                        queue.addFirst(blink + 1 to stringStone.substring(half).toLong())
                    }

                    else -> {
                        queue.addFirst(blink + 1 to stone * 2024)
                    }
                }
            }
        }


        return finishedStones
    }

    fun part1(input: List<String>): Int {
        return solve(input, 25)
    }


    fun part2(input: List<String>): Int {
        return solve(input, 75)
    }

    val sampleInput = """
        125 17
    """.trimIndent().lines()
    check(part1(sampleInput) == 55312)

    val input = readInput("2024/Day11")
    part1(input).println()

    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
