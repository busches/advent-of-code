package `2024`

import println
import readInput

private fun searchGrid(
    grid: List<String>,
    wordToFind: String,
    searchStrategy: Pair<Int, Int>
): List<List<Pair<Int, Int>>> {
    return grid.indices
        .filter { x -> searchIsInsideGrid(grid, wordToFind, x, searchStrategy.first) }
        .flatMap { x ->
            grid.indices
                .filter { y -> searchIsInsideGrid(grid, wordToFind, y, searchStrategy.second) }
                .map { y -> searchAtCoordinates(grid, wordToFind, searchStrategy, x, y) }
                .filter { it.isNotEmpty() }
                .flatten()
        }
        .chunked(wordToFind.length)
}

private fun searchAtCoordinates(
    grid: List<String>,
    wordToFind: String,
    searchStrategy: Pair<Int, Int>,
    x: Int,
    y: Int
): List<Pair<Int, Int>> {
    val wordFound = wordToFind.indices
        .all { letterPosition ->
            (grid[y + letterPosition * searchStrategy.second][x + letterPosition * searchStrategy.first].toString()
                    == wordToFind.substring(letterPosition, letterPosition + 1))
        }
    return if (wordFound) {
        wordToFind.indices.map { letterPosition ->
            x + letterPosition * searchStrategy.first + 1 to y + letterPosition * searchStrategy.second + 1
        }
    } else {
        listOf()
    }
}

private fun searchIsInsideGrid(grid: List<String>, wordToFind: String, coordinate: Int, increment: Int): Boolean {
    return coordinate + wordToFind.length * increment <= grid.size && coordinate + wordToFind.length * increment >= -1
}

fun main() {

    fun part1(input: List<String>): Int {
        val searchPatterns = listOf(
            0 to 1,
            0 to -1,
            1 to 0,
            -1 to 0,
            1 to 1,
            1 to -1,
            -1 to 1,
            -1 to -1
        )

        return searchPatterns.sumOf { searchPattern ->
            searchGrid(input, "XMAS", searchPattern)
                .count()
        }
    }


    fun part2(input: List<String>): Int {
        val searchPatterns = listOf(
            1 to 1,
            1 to -1,
            -1 to 1,
            -1 to -1
        )

        return searchPatterns
            .map { searchPattern ->
                searchGrid(input, "MAS", searchPattern)
                    .map { coordinates -> coordinates[1] /* we only care about the A */ }
            }
            .flatten()
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }
            .count()
    }

    val sampleInput = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().split("\n")
    check(part1(sampleInput) == 18)

    val input = readInput("2024/Day04")
    part1(input).println()

    check(part2(sampleInput) == 9)
    part2(input).println()
}
