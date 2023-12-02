package `2023`

import println
import readInput
import java.lang.IllegalArgumentException
import kotlin.math.max

fun main() {

    fun mapGameRow(input: String): Pair<Int, List<Pair<String, Int>>> {
        val (game, pulls) = input.split(":")
        val gameId = game.replace("Game ", "").toInt()
        val allColors = pulls.split(";")
            .flatMap { pull -> pull.split(",") }
            .map { colorDrawn ->
                val colorSize = "(\\d*) (.*)".toRegex()
                val (rawSize, color) = colorSize.find(colorDrawn.trim())!!.destructured
                val size = rawSize.toInt()
                Pair(color, size)
            }
        return Pair(gameId, allColors)
    }

    fun isGamePossible(input: String, red: Int, green: Int, blue: Int): Int {
        val (gameId, allColors) = mapGameRow(input)
        val gameMatches = allColors.all { (color, size) ->
                when (color) {
                    "red" -> if (red < size) return@all false
                    "green" -> if (green < size) return@all false
                    "blue" -> if (blue < size) return@all false
                    else -> throw IllegalArgumentException("Unexpected color")
                }
                true
            }

        return if (gameMatches) gameId else 0
    }

    check(isGamePossible("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green", 12, 13, 14) == 1)
    check(isGamePossible("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue", 12, 13, 14) == 2)
    check(isGamePossible("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red", 12, 13, 14) == 0)
    check(isGamePossible("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red", 12, 13, 14) == 0)
    check(isGamePossible("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green", 12, 13, 14) == 5)

    fun part1(input: List<String>, red: Int, green: Int, blue: Int): Int {
        return input.sumOf { isGamePossible(it, red, green, blue) }
    }

    check(
        part1(
            listOf(
                "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
            ),
            12, 13, 14
        ) == 8
    )


    fun minNeeded(input: String): Triple<Int, Int, Int> {
        var red = 0
        var green = 0
        var blue = 0
        val (gameId, allColors) = mapGameRow(input)
        allColors.forEach {(color, size) ->
            when (color) {
                "red" -> red = max(red, size)
                "green" -> green = max(green, size)
                "blue" -> blue = max(blue, size)
            }
        }

        return Triple(red, green, blue)
    }

    check(minNeeded("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green") == Triple(4, 2, 6))
    check(minNeeded("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue") == Triple(1, 3, 4))
    check(minNeeded("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red") == Triple(20, 13, 6))
    check(minNeeded("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red") == Triple(14, 3, 15))
    check(minNeeded("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green") == Triple(6, 3, 2))


    fun part2(input: List<String>): Int {
        return input.map { minNeeded(it) }
            .sumOf { (red, green, blue) -> red * green * blue }
    }

    check(
        part2(
            listOf(
                "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
            )
        ) == 2286
    )

    val input = readInput("2023/Day02")
    part1(input, 12, 13, 14).println()
    //2795

    part2(input).println()
    // 75561
}

