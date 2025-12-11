package `2025`

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
    
    data class Machine(val indicatorLights: IndicatorLights, val buttonWiring: List<List<Int>>, val joltageRequirements: List<Int>)
    
    
    data class MachineState(
        val indicatorLights: IndicatorLights,
        val buttonsToPress: List<Int>,
        val buttonPresses: Int = 1
    )
    
    fun part1(input: List<String>): Long {
        val machines = input.map { line ->
            val inputs = line.split(" ")
            var indicatorLights = IndicatorLights(emptyList())
            val buttonWiring = mutableListOf<List<Int>>()
            var joltageRequirements = emptyList<Int>()
            inputs.forEach { item ->
                when (item.first()) {
                    '[' -> indicatorLights = IndicatorLights(item.drop(1).dropLast(1).toList())
                    '(' -> buttonWiring.add(item.drop(1).dropLast(1).split(",").map { it.toInt() })
                    '{' -> joltageRequirements = item.drop(1).dropLast(1).split(",").map { it.toInt() }
                }
            }
            
            Machine(indicatorLights, buttonWiring, joltageRequirements)
        }
        
        return machines.sumOf { machine ->
            val buttons = machine.buttonWiring
            val desiredLights = machine.indicatorLights

            val machineQueue = PriorityQueue<MachineState>(compareBy { state -> state.buttonPresses })
            machineQueue.apply {
                buttons.forEach { add(MachineState(machine.indicatorLights.allOff(), it)) }
            }

            while (machineQueue.isNotEmpty()) {
                val (indicatorLights, buttonsToPress, buttonPresses) = machineQueue.remove()

                val newLights = indicatorLights.press(buttonsToPress)
                if (desiredLights == newLights) {
                    "Pressed $buttonPresses for $machine".println()
                    return@sumOf buttonPresses.toLong()
                }
                machineQueue.apply {
                    buttons.forEach { add(MachineState(newLights, it, buttonPresses + 1)) }
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
        
        
        TODO()
    }


    val input = readInput("2025/Day10")
    part1(input).println()

    check(part2(sampleInput.lines()) == 15L)

    part2(input).println()
}


