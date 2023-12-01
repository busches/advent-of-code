package `2015`

import println
import readInput
import kotlin.math.max

fun main() {

    fun <T> swapLights(
        x1: String,
        x2: String,
        y1: String,
        y2: String,
        lights: MutableMap<Pair<Int, Int>, T>,
        b: (Any, Any) -> T
    ) {
        for (x in (x1.toInt())..(x2.toInt())) {
            for (y in (y1.toInt())..(y2.toInt())) {
                lights[Pair(x, y)] = b(x, y)
            }
        }
    }

    fun <T> seedLights(initialValue: T): MutableMap<Pair<Int, Int>, T> {
        val lights = mutableMapOf<Pair<Int, Int>, T>()
        for (x in 0..999) {
            for (y in 0..999) {
                lights[Pair(x, y)] = initialValue
            }
        }
        return lights
    }

    val instructionRegex = "(turn on|toggle|turn off) (\\d*),(\\d*) through (\\d*),(\\d*)".toRegex()
    fun part1(input: List<String>): Int {
        val lights = seedLights(false)
        input.forEach { instruction ->
            val (command, x1, y1, x2, y2) = instructionRegex.find(instruction)!!.destructured
            when (command) {
                "turn on" -> swapLights(x1, x2, y1, y2, lights) { _, _ -> true }
                "toggle" -> swapLights(x1, x2, y1, y2, lights) { x, y -> !lights[Pair(x, y)]!! }
                "turn off" -> swapLights(x1, x2, y1, y2, lights) { _, _ -> false }
            }
        }
        return lights.filter { it.value }.size
    }
    check(part1(listOf("turn on 0,0 through 999,999")) == 1_000_000)
    check(part1(listOf("toggle 0,0 through 999,0")) == 1000)
    check(part1(listOf("turn on 0,0 through 999,999", "turn off 499,499 through 500,500")) == (1_000_000 - 4))

    fun part2(input: List<String>): Int {
        val lights = seedLights(0)
        input.forEach { instruction ->
            val (command, x1, y1, x2, y2) = instructionRegex.find(instruction)!!.destructured
            when (command) {
                "turn on" -> swapLights(x1, x2, y1, y2, lights) { x, y -> lights[Pair(x, y)]!! + 1 }
                "toggle" -> swapLights(x1, x2, y1, y2, lights) { x, y -> lights[Pair(x, y)]!! + 2 }
                "turn off" -> swapLights(x1, x2, y1, y2, lights) { x, y -> max(0, lights[Pair(x, y)]!! - 1) }
            }
        }
        return lights.map { it.value }.sum()
    }

    check(part2(listOf("turn on 0,0 through 0,0")) == 1)
    check(part2(listOf("toggle 0,0 through 999,999")) == 2_000_000)

    val input = readInput("2015/Day06")
    part1(input).println()
    part2(input).println()
}
