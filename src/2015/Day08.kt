package `2015`

import println
import readInput

fun main() {
    fun getCodeSize(s: String): Int {
        return s.length
    }

    fun getMemorySize(input: String): Int {
        return input
            .replace("^\"".toRegex(), "")
            .replace("\"$".toRegex(), "")
            .replace("\\\"", "A")
            .replace("\\\\", "B")
            .replace("\\\\x[0-9a-f]{2}".toRegex(), "#")
            .length
    }

    fun part1(input: List<String>): Int {
        val totalCodeSize = input.sumOf { getCodeSize(it) }
        val totalMemorySize = input.sumOf { getMemorySize(it) }
        return totalCodeSize - totalMemorySize
    }

    check(getCodeSize("\"\"") == 2)
    check(getMemorySize("\"\"").also { it.println() } == 0)

    check(getCodeSize("\"abc\"") == 5)
    check(getMemorySize("\"abc\"") == 3)

    check(getCodeSize("\"aaa\\\"aaa\"") == 10)
    check(getMemorySize("\"aaa\\\"aaa\"").also { it.println() } == 7)

    check(getCodeSize("\"\\x27\"") == 6)
    check(getMemorySize("\"\\x27\"").also { it.println() } == 1)


    check(part1(listOf("\"\"", "\"abc\"", "\"aaa\\\"aaa\"", "\"\\x27\"")) == 12)


    fun part2(input: List<String>): Int {
        var encodedSize = input.sumOf {
            it.map { character ->
                when (character) {
                    '\"' -> "\\\""
                    '\\' -> "\\\\"
                    else -> character.toString()
                }
            }.joinToString(prefix = "\"", postfix = "\"", separator = "").length
        }
        var codeSize = input.sumOf { getCodeSize(it) }
        return encodedSize - codeSize
    }

    val input = readInput("2015/Day08")
    part1(input).println()

    part2(input).println()
}

