package `2024`

import println
import readInput

fun main() {
    val mulRegex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()

    fun part1(input: List<String>): Int {
        val getAllMulRegex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
        return input.sumOf {
            getAllMulRegex.findAll(it).sumOf { match ->
                val (first, second) = mulRegex.find(match.value)!!.destructured
                first.toInt() * second.toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val getAllMulDoDontRegex = """mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)""".toRegex()

        var countMultipliers = true // this does not reset per LINE!
        return input.sumOf { line ->
            getAllMulDoDontRegex.findAll(line).sumOf { match ->
                when (match.value) {
                    "don't()" -> countMultipliers = false
                    "do()" -> countMultipliers = true
                    else -> {
                        if (countMultipliers) {
                            val (first, second) = mulRegex.find(match.value)!!.destructured
                            return@sumOf first.toInt() * second.toInt()
                        }
                    }
                }
                0
            }
        }
    }

    val sampleInput = listOf(
        "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    )
    check(part1(sampleInput) == 161)

    val input = readInput("2024/Day03")
    part1(input).println()

    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)
    part2(input).println()
}
