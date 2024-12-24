package `2024`

import utils.println
import utils.readInput

fun main() {
    val start = System.currentTimeMillis()

    fun getConnectionChain(allConnections: Map<String, Set<String>>, startingConnection: String): List<List<String>> {
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

    fun getGroupedConnections(input: List<String>): Map<String, Set<String>> {
        val connections = input.flatMap { line ->
            val (a, b) = line.split("-")
            listOf(a to b, b to a)
        }
        val groupedConnections = connections.groupBy({ it.first }, { it.second })
            .mapValues { it.value.toSet() }
        return groupedConnections
    }

    fun part1(input: List<String>): Int {
        val groupedConnections = getGroupedConnections(input)
        val onlyTConnections = groupedConnections.filter { it.key.startsWith("t") }

        return onlyTConnections
            .flatMap { (startingConnection, _) -> getConnectionChain(groupedConnections, startingConnection) }
            .toSet()
            .size
    }

    // This works for the sample input, but needs some kind of cache, which lead to Bron Kerbosch shennagins
    fun mapConnection(allConnections: Map<String, List<String>>, connection: String, mappedConnection: List<String>): List<String> {
        val nextConnections = allConnections[connection]
        return if (nextConnections == null) {
            if (mappedConnection.first() == mappedConnection.last()) {
                mappedConnection
            } else {
                emptyList()
            }
        } else {
            if (mappedConnection.contains(connection)) {
                // we looped around
                mappedConnection
            } else {
                nextConnections.map { mapConnection(allConnections, it, mappedConnection + connection) }.maxBy { it.size }
            }
        }
    }

    // well ok - https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
    fun bronKerbosch(
        p: MutableSet<String>,
        r: Set<String> = emptySet(),
        x: MutableSet<String> = mutableSetOf(),
        graph: Map<String, Set<String>>
    ): Set<Set<String>> {
        if (p.isEmpty() && x.isEmpty()) return setOf(r)

        return p.toList().flatMap { vertex ->
            bronKerbosch(
                r = r + vertex,
                p = p.intersect(graph[vertex]!!).toMutableSet(),
                x = x.intersect(graph[vertex]!!).toMutableSet(),
                graph = graph
            ).also {
                p -= vertex
                x += vertex
            }
        }.toSet()
    }

    fun part2(input: List<String>): String {
        val groupedConnections = getGroupedConnections(input)

        return bronKerbosch(groupedConnections.keys.toMutableSet(), graph = groupedConnections)
            .maxBy { it.size }
            .sorted()
            .joinToString(",")
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
    """.trimIndent().lines()
    check(part2(part2SampleInput) == "co,de,ka,ta")
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
