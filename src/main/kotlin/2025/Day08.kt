package `2025`

import utils.println
import utils.readInput
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {

    data class Coordinate3D(val x: Int, val y: Int, val z: Int)

    data class JunctionBox(val id: Int, val coordinate: Coordinate3D) {
        fun distance(other: JunctionBox): Double {
            return sqrt(
                (coordinate.x - other.coordinate.x).toDouble().pow(2)
                        + (coordinate.y - other.coordinate.y).toDouble().pow(2)
                        + (coordinate.z - other.coordinate.z).toDouble().pow(2)
            )
        }
    }

    class ConnectionManager(junctionBoxPositions: List<Coordinate3D>) {
        // Need to make our lookups O(N) and will use the index from the initial file for a unique id
        val boxes = junctionBoxPositions.mapIndexed(::JunctionBox)
        val junctionBoxesToCircuits: MutableMap<Int, Int> = mutableMapOf()
        val circuits = List(boxes.size) { index ->
            junctionBoxesToCircuits[index] = index
            mutableSetOf(index)
        }

        val closestPairs: Iterator<Pair<JunctionBox, JunctionBox>> = iterator {
            while (true) {
                boxes
                    .flatMap { boxes.map { other -> it to other } }
                    // Track everything by the smallest ID, as we'll get a,b and b,a in and this ensures we only process each once
                    // Also drops a,a for us
                    .filter { it.first.id < it.second.id }
                    .sortedBy { (a, b) -> a.distance(b) }
                    .let { pairs -> yieldAll(pairs) }
            }
        }

        fun joinWithCircuit(a: JunctionBox, b: JunctionBox) {
            val (circuitToGrow, circuitToShrink) = run {
                val circuitOfA = junctionBoxesToCircuits[a.id]!!
                val circuitOfB = junctionBoxesToCircuits[b.id]!!

                // Already same circuit
                if (circuitOfA == circuitOfB) {
                    return
                }

                if (circuits[circuitOfA].size >= circuits[circuitOfB].size) circuitOfA to circuitOfB
                else circuitOfB to circuitOfA
            }

            circuits[circuitToGrow].addAll(circuits[circuitToShrink])
            // Reassign all old boxes to the new group
            circuits[circuitToShrink].forEach { box -> junctionBoxesToCircuits[box] = circuitToGrow }
            circuits[circuitToShrink].clear()
        }

        fun connectNextClosest() {
            val (a, b) = closestPairs.next()
            joinWithCircuit(a, b)
        }
    }

    fun part1(input: List<String>, numberOfConnectionsToMake: Int): Long {
        val junctionBoxPositions = input.map {
            val (x, y, z) = it.split(",")
            Coordinate3D(x.toInt(), y.toInt(), z.toInt())
        }
        val cm = ConnectionManager(junctionBoxPositions)

        repeat(times = numberOfConnectionsToMake) {
            cm.connectNextClosest()
        }
        return cm.circuits
            .asSequence()
            .filter { circuit -> circuit.isNotEmpty() }
            .sortedByDescending { circuit -> circuit.size }
            .map { ids -> ids.map(cm.boxes::get).toSet() }
            .take(3)
            .map { it.size.toLong() }
            .reduce(Long::times)
    }

    val sampleInput = """
        162,817,812
        57,618,57
        906,360,560
        592,479,940
        352,342,300
        466,668,158
        542,29,236
        431,825,988
        739,650,466
        52,470,668
        216,146,977
        819,987,18
        117,168,530
        805,96,715
        346,949,466
        970,615,88
        941,993,340
        862,61,35
        984,92,344
        425,690,689
    """.trimIndent()
    check(part1(sampleInput.lines(), 10) == 40L)

    fun part2(input: List<String>): Long {
        TODO()
    }


    val input = readInput("2025/Day08")
    // 13200 too low
    part1(input, 1000).println()

    check(part2(sampleInput.lines()) == 40L)

    part2(input).println()
}
