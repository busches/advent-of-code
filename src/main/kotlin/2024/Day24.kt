package `2024`

import utils.println
import utils.readInput

fun main() {
    val start = System.currentTimeMillis()

    data class GateMapping(val inputGate1: String, val inputGate2: String, val operator: String)

    fun part1(input: List<String>): Long {
        val inputs = input.take(input.indexOf("")).associate {
            val (input, value) = it.split(": ")
            input to value.toInt()
        }
        val gateRegex = """(.*) (AND|XOR|OR) (.*) -> (.*)""".toRegex()
        val gates = input.takeLast(input.size - input.indexOf("") - 1).associate {
            val (inputGate1, operator, inputGate2, outputGate) = gateRegex.find(it)!!.destructured
            outputGate to GateMapping(inputGate1, inputGate2, operator)
        }

        val resolvedOutput = inputs.toMutableMap()

        val allZees = gates.filterKeys { it.startsWith("z") }.keys.toList().sortedDescending()

        fun getValue(gate: String): Int {
            return resolvedOutput.getOrPut(gate) {
                val (inputGate1, inputGate2, operator) = gates[gate]
                    ?: throw IllegalArgumentException("Unknown gate '$gate'")
                val gate1Value = getValue(inputGate1)
                val gate2Value = getValue(inputGate2)
                when (operator) {
                    "AND" -> gate1Value.and(gate2Value)
                    "XOR" -> gate1Value.xor(gate2Value)
                    "OR" -> gate1Value.or(gate2Value)
                    else -> throw IllegalArgumentException("Unknown operator: $operator")
                }
            }
        }

        // We need to resolve all z gates and convert that string from binary to decimal
        return allZees.map { getValue(it) }.joinToString("").also { it.println() }.toLong(2).also { it.println() }
    }


    fun part2(input: List<String>): Int {
        TODO("Need to implement Part 2")
    }

    val sampleInput = """
        x00: 1
        x01: 1
        x02: 1
        y00: 0
        y01: 1
        y02: 0
        
        x00 AND y00 -> z00
        x01 XOR y01 -> z01
        x02 OR y02 -> z02
    """.trimIndent().lines()
    check(part1(sampleInput) == 4L)

    val input = readInput("2024/Day24")
    part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
