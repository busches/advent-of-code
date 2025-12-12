package `2025`

import utils.anyIndexed
import utils.println
import utils.readInput
import java.util.PriorityQueue

fun main() {

    data class ConnectionPath(val path: List<String>, val nextDevice: String)

    fun part1(input: List<String>): Long {
        val devices = input.associate { line ->
            val device = line.take(3)
            val outputs = line.drop(5).split(' ')
            device to outputs
        }

        val start = "you"
        val end = "out"

        val queue = ArrayDeque<ConnectionPath>().apply {
            devices[start]!!.forEach { output -> add(ConnectionPath(listOf(start), output)) }
        }
        val paths = mutableSetOf<List<String>>()

        while (queue.isNotEmpty()) {
            val connectionPath = queue.removeFirst()
            if (connectionPath.nextDevice == end) {
                paths.add(connectionPath.path)
            } else {
                val nextDevice = connectionPath.nextDevice
                devices[nextDevice]!!.forEach { output ->
                    queue.add(
                        ConnectionPath(
                            connectionPath.path + nextDevice, output
                        )
                    )
                }
            }
        }

        return paths.size.toLong()
    }

    val sampleInput = """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
    """.trimIndent()
    check(part1(sampleInput.lines()) == 5L)

    fun part2(input: List<String>): Long {
        TODO()
    }

    val input = readInput("2025/Day11")
    part1(input).println()

    check(part2(sampleInput.lines()) == 33L)

    part2(input).println()
}


