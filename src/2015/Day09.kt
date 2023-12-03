package `2015`

import println
import readInput

fun main() {

    val citySplit = "(.*) to (.*) = (.*)".toRegex()

    fun buildRoutesAndCities(input: List<String>): Pair<Map<Pair<String, String>, Int>, List<String>> {
        val allRoutes = input
            .flatMap { route ->
                val (city1, city2, distance) = citySplit.find(route)!!.destructured
                listOf(
                    city1 to city2 to distance.toInt(),
                    city2 to city1 to distance.toInt(),
                )
            }.toMap()


        val allCities = allRoutes.map { it.key.first }.distinct()
        return Pair(allRoutes, allCities)
    }

    fun part1(input: List<String>): Int {
        val (allRoutes, allCities) = buildRoutesAndCities(input)
        return allCities.permutations().minOf { route ->
            route.zipWithNext().sumOf { allRoutes[it]!! }
        }
    }

    check(
        part1(
            listOf(
                "London to Dublin = 464",
                "London to Belfast = 518",
                "Dublin to Belfast = 141"
            )
        ) == 605
    )


    fun part2(input: List<String>): Int {
        val (allRoutes, allCities) = buildRoutesAndCities(input)
        return allCities.permutations().maxOf { route ->
            route.zipWithNext().sumOf { allRoutes[it]!! }
        }
    }

    check(
        part2(
            listOf(
                "London to Dublin = 464",
                "London to Belfast = 518",
                "Dublin to Belfast = 141"
            )
        ) == 982
    )

    val input = readInput("2015/Day09")
    part1(input).println()
    part2(input).println()
}


// Based off https://gist.github.com/trygvea/a2d9cdbc19ceff3df7eb64ccef3c0597
fun <T> List<T>.permutations(): List<List<T>> = when {
    size > 10 -> throw Exception("You probably dont have enough memory to keep all those permutations")
    size <= 1 -> listOf(this)
    else ->
        drop(1).permutations().map { perm ->
            (indices).map { i ->
                perm.subList(0, i) + first() + perm.drop(i)
            }
        }.flatten()
}

