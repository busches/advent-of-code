package `2015`

import utils.println

fun main() {

    fun transform(input: String): String {
        var count = 0
        var currentDigit = input.first()
        val output = StringBuilder()
        input.forEach {
            if (currentDigit == it) {
                count++
            } else {
                output.append("$count$currentDigit")
                currentDigit = it
                count = 1
            }
        }

        output.append("$count$currentDigit")

        return output.toString()
    }

    check(transform("1") == "11")
    check(transform("11") == "21")
    check(transform("21") == "1211")
    check(transform("1211") == "111221")
    check(transform("111221") == "312211")


    fun part1(): Int {
        var word = "1113222113"
        for (i in 1..40) {
            word = transform(word)
        }
        return word.length
    }


    fun part2(): Int {
        var word = "1113222113"
        for (i in 1..50) {
            word = transform(word)
        }
        return word.length
    }

    part1().println()
    part2().println()
}
