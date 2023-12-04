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

    fun findMaxHappiness(
        allPeople: List<String>,
        peopleMap: Map<Pair<String, String>, Int>
    ) = allPeople.permutations().maxOf { peopleCombo ->
        peopleCombo.zipWithNext().sumOf { (peopleMap[it] ?: 0) + (peopleMap[Pair(it.second, it.first)] ?: 0) } +
                // Have to add in the first and last people as sitting next to each other also
                (peopleMap[Pair(peopleCombo.first(), peopleCombo.last())] ?: 0) +
                (peopleMap[Pair(peopleCombo.last(), peopleCombo.first())] ?: 0)
    }

    fun part1(input: List<String>): Int {
        val data = input.map(::extractValues)

        val allPeople = data.flatMap { listOf(it.first.first, it.first.second) }.distinct()
        val peopleMap = data.toMap()

        return findMaxHappiness(allPeople, peopleMap)
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
        val data = input.map(::extractValues)

        val allPeople = data.flatMap { listOf(it.first.first, it.first.second) }.distinct() + listOf("Me")
        val peopleMap = data.toMap()

        return findMaxHappiness(allPeople, peopleMap)
    }

    part2(input).println()
}
