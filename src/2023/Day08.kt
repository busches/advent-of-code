package `2023`

import println
import readInput
import kotlin.IllegalArgumentException

fun main() {

    data class TheMap(val instructions: String, val nodes: Map<String, Pair<String, String>>)

    val NODE_REGEX = "(.*) = \\((.*), (.*)\\)".toRegex()

    fun extract(input: List<String>): TheMap {
        val instructions = input.first()

        val nodes = input.drop(2).associate { line ->
            val (key, node1, node2) = NODE_REGEX.find(line)!!.destructured
            key to (node1 to node2)
        }

        return TheMap(instructions, nodes)
    }

    fun part1(input: List<String>): Int {
        val (instructions, nodes) = extract(input)
        var steps = 0
        var currentNode = nodes["AAA"]!!

        while (currentNode != nodes["ZZZ"]) {
            instructions.forEach { instruction ->
                val nextNode = when (instruction) {
                    'L' -> currentNode.first
                    'R' -> currentNode.second
                    else -> throw IllegalArgumentException("$instruction")
                }
                currentNode = nodes[nextNode]!!
            }
            steps += instructions.length
        }

        return steps
    }

    check(part1(readInput("2023/Day08_Test")) == 6)

    val input = readInput("2023/Day08")
    part1(input).println()

    fun part2(input: List<String>): Int {
        TODO()
    }

    check(part2(readInput("2023/Day08_Test2")) == 6)
    part2(input).println()
}
