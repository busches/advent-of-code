package `2015`

import println
import readInput

fun main() {

    fun swapLights(
        x1: String,
        x2: String,
        y1: String,
        y2: String,
        lights: MutableMap<Pair<Int, Int>, Boolean>,
        b: (Any, Any) -> Boolean
    ) {
        for (x in (x1.toInt())..(x2.toInt())) {
            for (y in (y1.toInt())..(y2.toInt())) {
                lights[Pair(x, y)] = b(x, y)
            }
        }
    }

    fun seedLights(): MutableMap<Pair<Int, Int>, Boolean> {
        val lights = mutableMapOf<Pair<Int, Int>, Boolean>()
        for (x in 0..999) {
            for (y in 0..999) {
                lights[Pair(x, y)] = false
            }
        }
        return lights
    }

    fun part1(input: List<String>): Int {
        val lights = seedLights()
        val instructionRegex = "(turn on|toggle|turn off) (\\d*),(\\d*) through (\\d*),(\\d*)".toRegex()
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
        val twoCharactersRepeating = "([a-z]{2}).*\\1".toRegex()
        val twoCharactersMatchWithOneCharactersBetween = "([a-z]).\\1".toRegex()

        return input.filter { twoCharactersRepeating in it }
            .filter { twoCharactersMatchWithOneCharactersBetween in it }.size
    }

    check(part2(listOf("qjhvhtzxzqqjkmpb")) == 1)
    check(part2(listOf("xxyxx")) == 1)
    check(part2(listOf("uurcxstgmygtbstg")) == 0)
    check(part2(listOf("ieodomkazucvgmuy")) == 0)

    val input = readInput("2015/Day06")
    part1(input).println()
    part2(input).println()
}
