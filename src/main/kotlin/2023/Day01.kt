package `2023`

import utils.println
import utils.readInput

fun main() {

    val numbers = (0..9).map { "$it" }.toList()

    fun findNumberString(input: List<String>, strings: List<String>) = input.map {
        "${it.findAnyOf(strings)!!.second}${it.findLastAnyOf(strings)!!.second}"
    }

    fun part1(input: List<String>): Int {
        return findNumberString(input, numbers).sumOf { it.toInt() }
    }
    check(part1(listOf("1abc2")) == 12)
    check(part1(listOf("pqr3stu8vwx")) == 38)
    check(part1(listOf("a1b2c3d4e5f")) == 15)
    check(part1(listOf("treb7uchet")) == 77)

    val numbersAsWords = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val numbersAndWords = numbers + numbersAsWords
    fun part2(input: List<String>): Int {
        return findNumberString(input, numbersAndWords)
            .map { numberString ->
                numbersAsWords.foldIndexed(numberString) { index, cleanNumberString, number ->
                    cleanNumberString.replace(number, index.toString())
                }
            }.sumOf { it.toInt() }
    }

    check(part2(listOf("two1nine")) == 29)
    check(part2(listOf("eightwothree")) == 83)
    check(part2(listOf("abcone2threexyz")) == 13)
    check(part2(listOf("xtwone3four")) == 24)
    check(part2(listOf("4nineeightseven2")) == 42)
    check(part2(listOf("zoneight234")) == 14)
    check(part2(listOf("7pqrstsixteen")) == 76)

    val input = readInput("2023/Day01")
    part1(input).println()
    part2(input).println()
}
