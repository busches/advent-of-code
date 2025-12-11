package `2025`

import utils.anyIndexed
import utils.println
import utils.readInput
import java.util.PriorityQueue

fun main() {
    val off = '.'
    val on = '#'
    fun Char.toggle(): Char {
        return if (this == off) on else off
    }

    data class IndicatorLights(val lights: List<Char>) {
        fun allOff(): IndicatorLights {
            val newLights = lights.map { _ -> off }
            return IndicatorLights(newLights)
        }

        fun press(buttons: List<Int>): IndicatorLights {
            val newLights = lights.mapIndexed { index, light ->
                if (index in buttons) {
                    light.toggle()
                } else {
                    light
                }
            }
            return IndicatorLights(newLights)
        }
    }


    data class Joltage(val joltage: List<Int>) {
        fun start(): Joltage {
            return Joltage(joltage.map { _ -> 0 })
        }

        fun press(buttons: List<Int>): Joltage {
            val newJoltage = joltage.mapIndexed { index, currentJoltage ->
                if (index in buttons) {
                    currentJoltage + 1
                } else {
                    currentJoltage
                }
            }
            return Joltage(newJoltage)
        }

        fun exceeds(desiredJoltage: Joltage): Boolean {
            return joltage.anyIndexed { index, i -> (i > desiredJoltage.joltage[index]) }
        }
    }

    data class Machine(
        val indicatorLights: IndicatorLights,
        val buttonWiring: List<List<Int>>,
        val joltageRequirements: Joltage
    )

    data class StartupMachineState(
        val indicatorLights: IndicatorLights,
        val buttonsToPress: List<Int>,
        val buttonPresses: Int = 1
    )

    data class JoltageMachineState(
        val joltage: Joltage,
        val buttonsToPress: List<Int>,
        val buttonPresses: Int = 1
    )

    fun parseInputToMachines(input: List<String>): List<Machine> {
        val machines = input.map { line ->
            val inputs = line.split(" ")
            var indicatorLights = IndicatorLights(emptyList())
            val buttonWiring = mutableListOf<List<Int>>()
            var joltageRequirements = Joltage(emptyList())
            inputs.forEach { item ->
                when (item.first()) {
                    '[' -> indicatorLights = IndicatorLights(item.drop(1).dropLast(1).toList())
                    '(' -> buttonWiring.add(item.drop(1).dropLast(1).split(",").map { it.toInt() })
                    '{' -> joltageRequirements = Joltage(item.drop(1).dropLast(1).split(",").map { it.toInt() })
                }
            }

            Machine(indicatorLights, buttonWiring, joltageRequirements)
        }
        return machines
    }

    fun part1(input: List<String>): Long {
        val machines = parseInputToMachines(input)

        return machines.sumOf { machine ->
            val buttons = machine.buttonWiring
            val desiredLights = machine.indicatorLights

            val machineQueue = PriorityQueue<StartupMachineState>(compareBy { state -> state.buttonPresses })
            machineQueue.apply {
                buttons.forEach { add(StartupMachineState(machine.indicatorLights.allOff(), it)) }
            }

            while (machineQueue.isNotEmpty()) {
                val (indicatorLights, buttonsToPress, buttonPresses) = machineQueue.remove()

                val newLights = indicatorLights.press(buttonsToPress)
                if (desiredLights == newLights) {
                    "Pressed $buttonPresses for $machine".println()
                    return@sumOf buttonPresses.toLong()
                }
                machineQueue.apply {
                    buttons.forEach { add(StartupMachineState(newLights, it, buttonPresses + 1)) }
                }
            }
            TODO()
        }
    }

    val sampleInput = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent()
    check(part1(sampleInput.lines()) == 7L)

    fun part2(input: List<String>): Long {
        val machines = parseInputToMachines(input)

        return machines.sumOf { machine ->
            val buttons = machine.buttonWiring
            val desiredJoltage = machine.joltageRequirements


            // Joltage and Button Presses
            // If we have the same joltage and button presses, it doesn't matter how we got there, don't start it again
            val joltagesSeen = mutableSetOf<Pair<Joltage, Int>>()

            val machineQueue =
                PriorityQueue<JoltageMachineState>(compareBy<JoltageMachineState> { state -> state.buttonPresses }
                    .thenByDescending { state -> state.joltage.joltage.sum() })
            machineQueue.apply {
                buttons.forEach { add(JoltageMachineState(machine.joltageRequirements.start(), it)) }
            }

            while (machineQueue.isNotEmpty()) {
                val (joltage, buttonsToPress, buttonPresses) = machineQueue.remove()

                val newJoltage = joltage.press(buttonsToPress)
                if (!joltagesSeen.add(newJoltage to buttonPresses)) {
                    continue 
                }
                if (newJoltage.exceeds(desiredJoltage)) {
                    continue
                }

                if (desiredJoltage == newJoltage) {
                    "Pressed $buttonPresses for $machine".println()
                    return@sumOf buttonPresses.toLong()
                }
                machineQueue.apply {
                    buttons.forEach { add(JoltageMachineState(newJoltage, it, buttonPresses + 1)) }
                }
            }
            TODO()
        }
    }

    val input = readInput("2025/Day10")
//    part1(input).println()

    check(part2(sampleInput.lines()) == 33L)

    part2(input).println()
}


