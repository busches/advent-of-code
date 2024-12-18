package `2023`

import `2023`.Day20.Pulse.HIGH
import `2023`.Day20.Pulse.LOW
import utils.println
import utils.readInput

fun main() {
    check(Day20().part1(readInput("2023/Day20_Test")) == 32000000L).also { "Check Part1 passed".println() }
    check(Day20().part1(readInput("2023/Day20_Test2")) == 11687500L).also { "Check Part1-2 passed".println() }
    Day20().part1(readInput("2023/Day20")).println()
    Day20().part2(readInput("2023/Day20")).println()
}

class Day20 {
    fun part1(input: List<String>): Long {
        val modules = parseInput(input).toMutableMap()
        val pulseQueue = ArrayDeque<Triple<String, Pulse, String>>()
        // Could easily cache module state
        repeat(1000) {
            pulseQueue += Triple("button", LOW, "broadcaster")

            while (pulseQueue.isNotEmpty()) {
                val (sender, pulse, moduleName) = pulseQueue.removeFirst()
                val module = modules.getOrPut(moduleName) {
                    Untyped(moduleName)
                }
                pulseQueue.addAll(module.receive(sender, pulse))
            }
        }

        val totalLowPulses = modules.values.sumOf { it.lowPulses }
        val totalHighPulses = modules.values.sumOf { it.highPulses }

        return (totalLowPulses.toLong() * totalHighPulses).also { it.println() }
    }

    fun part2(input: List<String>): Long {
        val modules = parseInput(input).toMutableMap()

        // Find the inputs to rx's input, which is cl, and make them a new map to track button presses when it goes to High
        val rxInputs = modules.filterValues { it is Conjunction && it.receivers.contains("cl") }
            .mapValues { 0L }
            .toMutableMap()

        generateSequence(1) { it + 1 }.forEach { buttonPresses ->
            val pulseQueue = ArrayDeque<Triple<String, Pulse, String>>()
            pulseQueue += Triple("button", LOW, "broadcaster")
            while (pulseQueue.isNotEmpty()) {
                val (sender, pulse, moduleName) = pulseQueue.removeFirst()

                // Determine the first time each of our inputs get a High pulse
                if (rxInputs[sender] == 0L && pulse == HIGH) {
                    rxInputs[sender] = buttonPresses.toLong()
                    // If all have gotten a High, we can multiply them all to determine when it'd happen
                    if (rxInputs.values.all { it > 0L }) {
                        return rxInputs.values.fold(1L, Long::times)
                    }
                }

                val module = modules.getOrPut(moduleName) {
                    Untyped(moduleName)
                }
                pulseQueue.addAll(module.receive(sender, pulse))
            }
        }

        throw IllegalArgumentException("I'm broken")
    }

    private val moduleConfiguration = "[%&]?(.*) -> (.*)".toRegex()
    private fun parseInput(input: List<String>): Map<String, Module> {
        return input.map { line ->
            val (name, rawReceivers) = moduleConfiguration.find(line)!!.destructured
            val receivers = rawReceivers.split(",").map(String::trim)

            when (line.first()) {
                'b' -> Broadcaster(name, receivers)
                '%' -> FlipFlop(name, receivers)
                '&' -> {
                    val inputs = input.filter { it.contains(name) && !it.contains("&$name") }
                        .associate { moduleConfiguration.find(it)!!.destructured.component1() to LOW }
                        .toMutableMap()
                    Conjunction(name, receivers, inputs)
                }

                else -> throw IllegalArgumentException("What is this $line")
            }
        }.associateBy { it.name }
    }

    private enum class Pulse {
        LOW,
        HIGH
    }

    private interface Module {
        val name: String
        var lowPulses: Int
        var highPulses: Int
        fun receive(sender: String, pulse: Pulse): List<Triple<String, Pulse, String>>
    }

    private data class FlipFlop(
        override val name: String,
        private val receivers: List<String>,
        override var lowPulses: Int = 0,
        override var highPulses: Int = 0,
        private var onOff: Boolean = false
    ) : Module {
        override fun receive(sender: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
            val pulsesToSend = mutableListOf<Triple<String, Pulse, String>>()
            when (pulse) {
                LOW -> {
                    pulsesToSend += when (onOff) {
                        true -> receivers.map { Triple(name, LOW, it) }
                        false -> receivers.map { Triple(name, HIGH, it) }
                    }
                    onOff = !onOff
                    lowPulses++
                }

                HIGH -> highPulses++
            }
            return pulsesToSend
        }
    }

    private data class Conjunction(
        override val name: String,
        val receivers: List<String>,
        private var lastPulse: MutableMap<String, Pulse>,
        override var lowPulses: Int = 0,
        override var highPulses: Int = 0
    ) : Module {
        override fun receive(sender: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
            when (pulse) {
                LOW -> lowPulses++
                HIGH -> highPulses++
            }
            lastPulse[sender] = pulse

            val pulseToSend = if (lastPulse.all { (_, value) -> value == HIGH }) LOW else HIGH

            return receivers.map { Triple(name, pulseToSend, it) }
        }
    }

    private data class Broadcaster(
        override val name: String,
        private val receivers: List<String>,
        override var lowPulses: Int = 0,
        override var highPulses: Int = 0,
    ) : Module {
        override fun receive(sender: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
            when (pulse) {
                LOW -> lowPulses++
                HIGH -> highPulses++
            }
            return receivers.map { Triple(name, pulse, it) }
        }
    }

    private data class Untyped(
        override val name: String,
        override var lowPulses: Int = 0,
        override var highPulses: Int = 0,
    ) : Module {
        override fun receive(sender: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
            when (pulse) {
                LOW -> lowPulses++
                HIGH -> highPulses++
            }
            return emptyList()
        }

    }
}
