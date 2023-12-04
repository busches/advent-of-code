package `2015`

import println
import readInput
import utils.permutations

fun main() {

    fun extractValues(input: String): Pair<Pair<String, String>, Int> {
        val extract = "(.*) would (.*) (\\d*) happiness units by sitting next to (.*)\\.".toRegex()
        val (name1, gainOrLose, units, name2) = extract.find(input)!!.destructured

        return Pair(Pair(name1, name2), if (gainOrLose == "gain") units.toInt() else units.toInt() * -1)
    }

    fun Pair<String, String>.reverse() = Pair(second, first)

    fun findMaxHappiness(
        allPeople: List<String>,
        peopleMap: Map<Pair<String, String>, Int>
    ) = allPeople.permutations()
        .map { it + it.first() } // Add in first person again, as we need last + first person sitting next to each other
        .maxOf { peopleCombo -> peopleCombo.zipWithNext().sumOf { (peopleMap[it] ?: 0) + (peopleMap[it.reverse()] ?: 0) }
    }

    fun part1(input: List<String>): Int {
        val data = input.associate(::extractValues)
        val allPeople = data.keys.flatMap { listOf(it.first, it.second) }.distinct()
        return findMaxHappiness(allPeople, data)
    }

    check(
        part1(
            listOf(
                "Alice would gain 54 happiness units by sitting next to Bob.",
                "Alice would lose 79 happiness units by sitting next to Carol.",
                "Alice would lose 2 happiness units by sitting next to David.",
                "Bob would gain 83 happiness units by sitting next to Alice.",
                "Bob would lose 7 happiness units by sitting next to Carol.",
                "Bob would lose 63 happiness units by sitting next to David.",
                "Carol would lose 62 happiness units by sitting next to Alice.",
                "Carol would gain 60 happiness units by sitting next to Bob.",
                "Carol would gain 55 happiness units by sitting next to David.",
                "David would gain 46 happiness units by sitting next to Alice.",
                "David would lose 7 happiness units by sitting next to Bob.",
                "David would gain 41 happiness units by sitting next to Carol."
            )
        ) == 330
    )

    val input = readInput("2015/Day13")
    part1(input).println()


    fun part2(input: List<String>): Int {
        val data = input.associate(::extractValues)
        val allPeople = data.keys.flatMap { listOf(it.first, it.second) }.distinct() + listOf("Me")
        return findMaxHappiness(allPeople, data)
    }

    part2(input).println()
}
