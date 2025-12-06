package `2025`

import utils.println
import utils.readInput
import utils.transpose

fun main() {
    fun part1(input: List<String>): Long {
        val rows = input.map { line -> line.split("\\s+".toRegex()).filter { it.isNotBlank() } }.transpose()

        return rows.sumOf { row ->
            val operator = row.last()
            val operation = if (operator == "*")
                { a: Long, b: Long -> a * b } else { a: Long, b: Long -> a + b }
            val numbers = row.dropLast(1).map { it.toLong() }

            val initial = if (operator == "*") 1L else 0L
            numbers.fold(initial) { acc, value -> operation(acc, value) }
        }
    }


    val sampleInput = """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +  
    """.trimIndent()
    check(part1(sampleInput.lines()) == 4277556L)


    data class OpsToPerform(val operator: String, val numbers: List<Long>)
    
    fun chunkList(inputList: List<List<Char>>): List<List<List<Char>>> {
        val result = mutableListOf<MutableList<List<Char>>>()
        var currentSublist = mutableListOf<List<Char>>()
        for (item in inputList) {
            if (item.all { it.isWhitespace() }) {
                result.add(currentSublist)
                currentSublist = mutableListOf()
            } else {
                currentSublist.add(item)
            }
        }
        // Don't forget last group
        result.add(currentSublist)

        return result
    }

    fun part2(input: List<String>): Long {
        val rawData = input.map { line -> line.toList() }.transpose()
        val opsToPerform = chunkList(rawData)
            .map { it.filter { !it.all { it.isWhitespace() } } }
            .map {
                it.fold(OpsToPerform("?", emptyList())) { acc, chars ->
                    var operator = acc.operator
                    var numbers = chars.toList()
                    if (chars.last() == '*' || chars.last() == '+') {
                        operator = chars.last().toString()
                        numbers = chars.dropLast(1)
                    }
                    val newNumber = numbers.filter { !it.isWhitespace() }.joinToString("").toLong()
                    OpsToPerform(operator, acc.numbers + newNumber)
                }
            }

        return opsToPerform.sumOf { (operator, numbers) ->
            val initial = if (operator == "*") 1L else 0L
            val operation = if (operator == "*")
                { a: Long, b: Long -> a * b } else { a: Long, b: Long -> a + b }
            numbers.fold(initial) { acc, value -> operation(acc, value) }
        }
    }

    val input = readInput("2025/Day06")
    part1(input).println()

    check(part2(sampleInput.lines()) == 3263827L)

    part2(input).println()
}
