package `2025`

import utils.println
import utils.readInput

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

    data class Part2(val startDevice: String, val visitedDAC: Boolean, val visitedFFT: Boolean)

    fun part2(input: List<String>): Long {
        val devices = input.associate { line ->
            val device = line.take(3)
            val outputs = line.drop(5).split(' ')
            device to outputs
        }

        val start = "svr"
        val end = "out"
        val memo = mutableMapOf<Part2, Long>()
        fun findNextPath(path: List<String>, nextDevice: String, visitedDAC: Boolean, visitedFFT: Boolean): Long {
            return memo.getOrPut(Part2(nextDevice, visitedDAC, visitedFFT)) {
                return@getOrPut if (nextDevice == end) {
                    if (visitedDAC && visitedFFT) 1 else 0
                } else {
                    val path = path + nextDevice
                    val visitedDAC = visitedDAC || nextDevice == "dac"
                    val visitedFFT = visitedFFT || nextDevice == "fft"
                    devices[nextDevice]!!.fold(0L) { acc, nextDevice ->
                        acc + findNextPath(
                            path = path,
                            nextDevice = nextDevice,
                            visitedDAC = visitedDAC,
                            visitedFFT = visitedFFT,
                        )
                    }
                }
            }
        }
        return findNextPath(path = emptyList(), nextDevice = start, visitedDAC = false, visitedFFT = false)
    }

    val input = readInput("2025/Day11")
    part1(input).println()

    val sampleInput2 = """
        svr: aaa bbb
        aaa: fft
        fft: ccc
        bbb: tty
        tty: ccc
        ccc: ddd eee
        ddd: hub
        hub: fff
        eee: dac
        dac: fff
        fff: ggg hhh
        ggg: out
        hhh: out
    """.trimIndent()

    check(part2(sampleInput2.lines()) == 2L)

    part2(input).println()
}


