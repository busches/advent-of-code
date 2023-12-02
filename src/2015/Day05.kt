package `2015`

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val badLettersRegex = "ab|cd|pq|xy".toRegex()
        val doubleLetterRegex = "([a-z])\\1".toRegex()
        val threeVowels = "([^aeiou]*[aeiou]){3}".toRegex()

        return input
            .filterNot { badLettersRegex in it }
            .filter { doubleLetterRegex in it }
            .filter { threeVowels in it }.size
    }
    check(part1(listOf("ugknbfddgicrmopn")) == 1)
    check(part1(listOf("aaa")) == 1)
    check(part1(listOf("haegwjzuvuyypxyu")) == 0)
    check(part1(listOf("jchzalrnumimnmhp")) == 0)
    check(part1(listOf("dvszwmarrgswjxmb")) == 0)


    fun part2(input: List<String>): Int {
        val twoCharactersRepeating = "([a-z]{2}).*\\1".toRegex()
        val twoCharactersMatchWithOneCharactersBetween = "([a-z]).\\1".toRegex()

        return input.filter { twoCharactersRepeating in it }
            .filter { twoCharactersMatchWithOneCharactersBetween in it }.size
    }

    check(part2(listOf("qjhvhtzxzqqjkmpb")) == 1)
    check(part2(listOf("xxyxx")) == 1)
    check(part2(listOf("uurcxstgmygtbstg")) == 0)
    check(part2(listOf("ieodomkazucvgmuy")) == 0)

    val input = readInput("2015/Day05")
    part1(input).println()
    part2(input).println()
}
