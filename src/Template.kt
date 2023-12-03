import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        TODO()
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
        TODO()
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
