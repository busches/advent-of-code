import println
import readInput

fun main() {
    val start = System.currentTimeMillis()

    fun part1(input: List<String>): Int {
        TODO()
    }


    fun part2(input: List<String>): Int {
        TODO()
    }

    val sampleInput = """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
    """.trimIndent().lines()
    check(part1(sampleInput) == 605)

    val input = readInput("2024/Day09")
    part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
