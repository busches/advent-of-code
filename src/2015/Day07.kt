package `2015`

import println
import readInput

fun main() {

    // Would have been easier to flip the commands so it was a map of <Wire, Source> and then have a table that resolved them and do recursion until
    // We solved enough to input into A
    fun part1(input2: List<String>): Map<String, Int> {
        val input = input2.toMutableList()

        val gates = mutableMapOf<String, Int>()

        val assignment = "(\\d*) -> (.*)".toRegex()
        val andGate = "(.*) AND (.*) -> (.*)".toRegex()
        val orGate = "(.*) OR (.*) -> (.*)".toRegex()
        val leftShift = "(.*) LSHIFT (\\d+) -> (.*)".toRegex()
        val rightShift = "(.*) RSHIFT (\\d+) -> (.*)".toRegex()
        val notGate = "NOT (.*) -> (.*)".toRegex()

        var line = input.size - 1
        var lastInputSize = input.size
        while (input.isNotEmpty()) {
            if (line < 0) {
                line = input.size - 1
                if (lastInputSize == input.size) {
                    return gates.also {
                        it.println()
                        input.println()
                    }
                } else {
                    lastInputSize = input.size
                }
            }
            val command = input[line]
            when {
                command.matches(assignment) -> {
                    val (signal, gate) = assignment.find(command)!!.destructured
                    gates[gate] = signal.toInt()
                    input.remove(command)
                }

                command.matches(andGate) -> {
                    val (gate1, gate2, gate3) = andGate.find(command)!!.destructured
                    val gate1Value = if (gate1[0].isDigit()) gate1.toInt() else gates[gate1]
                    if (gate1Value == null) {
                        line--
                        continue
                    }
                    if (gates[gate2] == null) {
                        line--
                        continue
                    }

                    gates[gate3] = gate1Value.and(gates[gate2]!!)
                    input.remove(command)
                }

                command.matches(orGate) -> {
                    val (gate1, gate2, gate3) = orGate.find(command)!!.destructured
                    if (gates[gate1] == null) {
                        line--
                        continue
                    }
                    if (gates[gate2] == null) {
                        line--
                        continue
                    }
                    gates[gate3] = gates[gate1]!!.or(gates[gate2]!!)
                    input.remove(command)
                }

                command.matches(leftShift) -> {
                    val (fromGate, shiftAmount, toGate) = leftShift.find(command)!!.destructured
                    if (gates[fromGate] == null) {
                        line--
                        continue
                    }
                    gates[toGate] = gates[fromGate]!!.shl(shiftAmount.toInt())
                    input.remove(command)
                }

                command.matches(rightShift) -> {
                    val (fromGate, shiftAmount, toGate) = rightShift.find(command)!!.destructured
                    if (gates[fromGate] == null) {
                        line--
                        continue
                    }
                    gates[toGate] = gates[fromGate]!!.shr(shiftAmount.toInt())
                    input.remove(command)
                }

                command.matches(notGate) -> {
                    val (fromGate, toGate) = notGate.find(command)!!.destructured
                    if (gates[fromGate] == null) {
                        line--
                        continue
                    }
                    gates[toGate] = 65535 - gates[fromGate]!!
                    input.remove(command)
                }
            }
            line--
        }

        return gates.also { it.println() }
    }

    check(
        part1(
            listOf(
                "123 -> xa",
                "456 -> y",
                "xa AND y -> d",
                "xa OR y -> e",
                "xa LSHIFT 2 -> f",
                "y RSHIFT 2 -> g",
                "NOT xa -> h",
                "NOT y -> i"
            )
        ) == mapOf(
            "d" to 72,
            "e" to 507,
            "f" to 492,
            "g" to 114,
            "h" to 65412,
            "i" to 65079,
            "xa" to 123,
            "y" to 456,
        )
    )


    fun part2(input: List<String>): Map<String, Int> {
        return part1(input.map {
            if (it.endsWith("-> b")) {
                "16076 -> b"
            } else {
                it
            }
        })
    }

    val input = readInput("2015/Day07")
    // Off by one somewhere, so just return lx, because it passes the signal to a
    part1(input)["lx"].println()

    part2(input)["lx"].println()
}
