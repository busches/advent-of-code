package `2024`

import utils.println
import utils.readInput

fun main() {
    val start = System.currentTimeMillis()

    fun getConnectionChain(allConnections: Map<String, List<String>>, startingConnection: String): List<List<String>> {
        var connections = listOf(listOf(startingConnection))

        while (connections.first().count() < 3) {
            connections = connections.flatMap { connectionChain ->
                allConnections[connectionChain.last()]!! // Take the last chain and get the next possible ones
                    .filter { nextConnection -> !connectionChain.contains(nextConnection) }
                    .map { connectionChain + it }
            }
        }

        // Ensure all chains are linked to each other
        return connections.filter { connectionChain ->
            connectionChain.all { first ->
                connectionChain.all { second ->
                    allConnections[first]!!.contains(second) || first == second
                }
            }
        }.map { it.sorted() }
    }

    fun part1(input: List<String>): Int {
        val connections = input.flatMap { line ->
            val (a, b) = line.split("-")
            listOf(a to b, b to a)
        }
        val groupedConnections = connections.groupBy({ it.first }, { it.second })
        val onlyTConnections = groupedConnections.filter { it.key.startsWith("t") }

        return onlyTConnections
            .flatMap { (startingConnection, _) -> getConnectionChain(groupedConnections, startingConnection) }
            .toSet()
            .size
    }


    fun part2(input: List<String>): String {
        TODO("Need to implement Part 2")
    }

    val sampleInput = """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
    """.trimIndent().lines()
    check(part1(sampleInput) == 7)

    val input = readInput("2024/Day23")
    part1(input).println()

    val part2SampleInput = """
        ka-co
        ta-co
        de-co
        ta-ka
        de-ta
        ka-de
    """.trimIndent()
    check(part2(sampleInput) == "co,de,ka,ta")
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
