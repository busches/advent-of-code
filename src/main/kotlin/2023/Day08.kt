package `2023`

import utils.println
import utils.readInput

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

    fun stepsToNode(
        nodes: Map<String, Pair<String, String>>,
        instructions: String,
        startingNodeKey: String,
        endsWith: String,
    ): Int {
        var steps = 0
        var currentNode = nodes[startingNodeKey]!!
        var lastNodeKey = startingNodeKey

        while (!lastNodeKey.endsWith(endsWith)) {
            instructions.forEach { instruction ->
                val nextNode = if (instruction == 'L') currentNode.first else currentNode.second
                currentNode = nodes[nextNode]!!
                lastNodeKey = nextNode
            }
            steps += instructions.length
        }

        return steps
    }

    fun part1(input: List<String>): Int {
        val (instructions, nodes) = extract(input)
        return stepsToNode(nodes, instructions, "AAA", "ZZZ")
    }

    check(part1(readInput("2023/Day08_Test")) == 6)

    val input = readInput("2023/Day08")
    part1(input).println()

    fun factorsOfNumber(startingNumber: Int): List<Int> {
        val factors = mutableListOf<Int>()
        var number = startingNumber
        for (i in 2 until number) {
            while (number % i == 0) {
                factors.add(i)
                number /= i
            }
        }
        return factors
    }

    fun part2(input: List<String>): Long {
        val (instructions, nodes) = extract(input)

        val allStartingNodes = nodes.keys.filter { it.endsWith('A') }

        return allStartingNodes
            .map { nodeKey -> stepsToNode(nodes, instructions, nodeKey, "Z") }
            .flatMap { factorsOfNumber(it) }
            .toSet()
            .fold(1L, Long::times)
    }

    check(part2(readInput("2023/Day08_Test2")) == 6L)
    part2(input).println()
}
