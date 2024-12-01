package `2024`

import println
import readInput
import kotlin.math.abs

fun main() {

    fun getLists(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()
        val extract = "(\\d+)\\s*(\\d+)".toRegex()
        input.forEach {
            val (item1, item2) = extract.find(it)!!.destructured
            list1.add(item1.toInt())
            list2.add(item2.toInt())
        }
        list1.sort()
        list2.sort()
        return Pair(list1, list2)
    }

    fun part1(input: List<String>): Int {
        val (list1, list2) = getLists(input)

        var distance = 0;
        for (i in 0.rangeTo(list1.lastIndex)) {
            distance += abs(list1[i] - list2[i])
        }

        return distance
    }

    val sampleInput = listOf(
        "3   4",
        "4   3",
        "2   5",
        "1   3",
        "3   9",
        "3   3"
    )
    check(part1(sampleInput) == 11)


    fun part2(input: List<String>): Int {
        val (list1, list2) = getLists(input)

        var score = 0
        for (item in list1) {
            score += item * list2.count { it == item }
        }

        return score
    }

    val input = readInput("2024/Day01")
    part1(input).println()

    check(part2(sampleInput) == 31)

    part2(input).println()
}
