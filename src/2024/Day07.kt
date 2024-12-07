package `2024`

import println
import readInput

fun main() {
    val start = System.currentTimeMillis()

    val numberRegex = """\d+""".toRegex()
    fun part1(input: List<String>): Long {
        val equations = input.map { line ->
            val total = line.substringBefore(":").toLong()
            val numbers = numberRegex.findAll(line.substringAfter(":")).map {
                it.value.toLong()
            }.toList()
            total to numbers
        }

        return equations.flatMap { (total, numbers) ->
            val mutableNumbers = numbers.toMutableList()
            var runningTotals  = listOf(mutableNumbers.removeFirst())

            while(mutableNumbers.isNotEmpty()) {
                val nextNumber = mutableNumbers.removeFirst()
                runningTotals = runningTotals.flatMap { runningTotal ->
                    val addTotal = (runningTotal + nextNumber).let {
                        if (it > total) null  else it
                    }
                    val mulTotal = (runningTotal * nextNumber).let {
                        if (it > total) null  else it
                    }
                    listOfNotNull(addTotal, mulTotal)
                }
            }
            runningTotals.filter { it == total }.toSet() // Set as there can be more than one way to calculate it
        }.sum()

    }

    fun part2(input: List<String>): Long {
        val equations = input.map { line ->
            val total = line.substringBefore(":").toLong()
            val numbers = numberRegex.findAll(line.substringAfter(":")).map {
                it.value.toLong()
            }.toList()
            total to numbers
        }

        return equations.flatMap { (total, numbers) ->
            val mutableNumbers = numbers.toMutableList()
            var runningTotals  = listOf(mutableNumbers.removeFirst())

            while(mutableNumbers.isNotEmpty()) {
                val nextNumber = mutableNumbers.removeFirst()
                runningTotals = runningTotals.flatMap { runningTotal ->
                    val addTotal = (runningTotal + nextNumber).let {
                        if (it > total) null  else it
                    }
                    val mulTotal = (runningTotal * nextNumber).let {
                        if (it > total) null  else it
                    }
                    val concatTotal = (runningTotal.toString() + nextNumber.toString()).toLong().let {
                        if (it > total) null  else it
                    }
                    listOfNotNull(addTotal, mulTotal, concatTotal)
                }
            }
            runningTotals.filter { it == total }.toSet() // Set as there can be more than one way to calculate it
        }.sum()
    }

    val sampleInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()
    check(part1(sampleInput) == 3749L)

    val input = readInput("2024/Day07")
    part1(input).println()

    check(part2(sampleInput) == 11387L)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
