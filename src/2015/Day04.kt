package `2015`

import md5
import println
import readInput



fun main() {


    fun findHash(secretKey: String, prefix: String): Int =
        generateSequence(0) { it + 1 }.first {
            "$secretKey$it".md5().startsWith(prefix)
        }

    // MD5s that start with 00000
    fun part1(secretKey: String): Int {
        return findHash(secretKey, "00000")
    }
    val input = readInput("2015/Day04")
    check(part1("abcdef") == 609043)
    check(part1("pqrstuv") == 1048970)

    fun part2(secretKey: String): Int {
        return findHash(secretKey, "000000")
    }
    part1(input.first()).println()
    part2(input.first()).println()
}
