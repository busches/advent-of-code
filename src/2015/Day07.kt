package `2015`

import println
import readInput

fun main() {

    fun part1(input: List<String>, signalToFind: String): Int {
        val assignment = "(\\d*)".toRegex()
        val andGate = "(.*) AND (.*)".toRegex()
        val orGate = "(.*) OR (.*)".toRegex()
        val leftShift = "(.*) LSHIFT (\\d+)".toRegex()
        val rightShift = "(.*) RSHIFT (\\d+)".toRegex()
        val notGate = "NOT (.*)".toRegex()
        val commandRegex = "(.*) -> (.*)".toRegex()

        val signalMappings = input
            .map { mapping ->
                val (command, signal) = commandRegex.find(mapping)!!.destructured
                Pair(signal, command)
            }
            .associate { it }

        val calculatedSignals = mutableMapOf<String, Int>()

        fun calculateSignal(signalToFind: String): Int {
            val value = if (calculatedSignals[signalToFind] != null) {
                calculatedSignals[signalToFind]
            } else {
                val command = signalMappings[signalToFind]!!
                when {
                    command.matches(assignment) -> {
                        val (signal) = assignment.find(command)!!.destructured
                        signal.toInt()
                    }

                    command.matches(andGate) -> {
                        val (gate1, gate2) = andGate.find(command)!!.destructured
                        val gate1Value =
                            (if (gate1[0].isDigit()) gate1.toInt() else calculatedSignals[gate1]) ?: calculateSignal(
                                gate1
                            )
                        val gate2Value = calculatedSignals[gate2] ?: calculateSignal(gate2)

                        gate1Value.and(gate2Value)
                    }

                    command.matches(orGate) -> {
                        val (gate1, gate2) = orGate.find(command)!!.destructured
                        val gate1Value = calculatedSignals[gate1] ?: calculateSignal(gate1)
                        val gate2Value = calculatedSignals[gate2] ?: calculateSignal(gate2)

                        gate1Value.or(gate2Value)
                    }

                    command.matches(leftShift) -> {
                        val (fromGate, shiftAmount) = leftShift.find(command)!!.destructured
                        val fromGateValue = calculatedSignals[fromGate] ?: calculateSignal(fromGate)
                        fromGateValue.shl(shiftAmount.toInt())
                    }

                    command.matches(rightShift) -> {
                        val (fromGate, shiftAmount) = rightShift.find(command)!!.destructured
                        val fromGateValue = calculatedSignals[fromGate] ?: calculateSignal(fromGate)
                        fromGateValue.shr(shiftAmount.toInt())
                    }

                    command.matches(notGate) -> {
                        val (fromGate) = notGate.find(command)!!.destructured
                        val fromGateValue = calculatedSignals[fromGate] ?: calculateSignal(fromGate)
                        65535 - fromGateValue
                    }

                    else -> {
                        "in the else".println()
                        calculatedSignals[command] ?: calculateSignal(command)
                    }
                }
            }

            // Set it here, as computeIfAbsent doesn't allow nested mapping
            calculatedSignals[signalToFind] = value!!
            return value
        }
        return calculateSignal(signalToFind)
    }


    fun part2(input: List<String>): Int {
        return part1(input.map {
            if (it.endsWith("-> b")) {
                "16076 -> b"
            } else {
                it
            }
        }, "a")
    }

    val input = readInput("2015/Day07")
    // Off by one somewhere, so just return lx, because it passes the signal to a
    part1(input, "a").println()
    // 16076

    part2(input).println()
    //2797
}
