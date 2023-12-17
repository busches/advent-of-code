package `2023`

import println
import readInput
import utils.transpose

fun main() {

    data class Puzzle(val rows: List<String>, val part1Score: Int = -1) {
        override fun toString(): String {
            return "Puzzle\n${rows.joinToString("\n")}"
        }
    }

    fun extractPuzzles(input: List<String>): List<Puzzle> {
        val puzzles = mutableListOf<Puzzle>()

        val puzzleRows = mutableListOf<String>()
        for (line in input) {
            if (line.isBlank()) {
                puzzles.add(Puzzle(puzzleRows.toList()))
                puzzleRows.clear()
            } else {
                puzzleRows.add(line)
            }
        }
        puzzles.add(Puzzle(puzzleRows.toList()))

        return puzzles
    }


    fun getReflectionLine(newPuzzle: List<String>): Int {
//        "Searching line in this puzzle\n${newPuzzle.joinToString("\n")}".println()
        for (index in 0..(newPuzzle.size - 2)) {
            if (newPuzzle[index] == newPuzzle[index + 1]) {
//                "$index and ${index + 1} match".println()
                for (nextRowCounter in 1..<(newPuzzle.size - index)) {
                    val topRow = index - nextRowCounter
                    val bottomRow = index + 1 + nextRowCounter
                    if (topRow !in newPuzzle.indices || bottomRow !in newPuzzle.indices) {
//                        "Reached the end, all rows matched, ${newPuzzle.indices}, $topRow $bottomRow".println()
                        return index + 1
                    } else {
                        if (newPuzzle[topRow] != newPuzzle[bottomRow]) {
//                            "Rows $topRow and $bottomRow did not match".println()
                            break
                        } else {
//                            "Rows $topRow and $bottomRow matched".println()
                        }
                    }
                }
            }
        }
        return 0
    }

    fun scorePuzzle(puzzle: Puzzle): Puzzle {
        var score = getReflectionLine(puzzle.rows) * 100
//        "Horizontal Score $score".println()

        if (score == 0) {
            val verticalPuzzle = puzzle.rows.map { it.toList() }.transpose().map { it.joinToString("") }
            score = getReflectionLine(verticalPuzzle)

//            "Vertical Score $score".println()
        }
        return puzzle.copy(part1Score = score)
    }

    fun part1(input: List<String>): Int {
        return extractPuzzles(input)
            .sumOf { puzzle ->
                scorePuzzle(puzzle).part1Score//.also { it.println() }
            }
    }

    check(part1(readInput("2023/Day13_Test")) == 405)

    val input = readInput("2023/Day13")
    part1(input).println()

    fun part2(input: List<String>): Int {
        val originalPuzzles = extractPuzzles(input).map { puzzle -> scorePuzzle(puzzle) }

        return originalPuzzles.mapIndexed{ index, puzzle ->
            "".println()
            "Puzzle $index".println()
            "".println()
            for (row in puzzle.rows.indices) {
                for (column in puzzle.rows[row].indices) {
                    val character = puzzle.rows[row][column]
                    val newRow = StringBuilder(puzzle.rows[row]).apply {setCharAt(column, if (character == '#') '.' else '#')}.toString()
                    val newPuzzleRows = puzzle.rows.mapIndexed { index, puzzleRow ->
                        if (index == row) {
                            newRow
                        } else {
                            puzzleRow
                        }
                    }

                    val newScore = scorePuzzle(puzzle.copy(rows = newPuzzleRows).also { "Updated Puzzle for $row, $column\n $it".println() }).part1Score
                    if (newScore > 0 && newScore != puzzle.part1Score) {
                        return@mapIndexed newScore.also { "Found new score for Puzzle $index".println() }
                    }
                }
            }
//            puzzle.part1Score
            // too low 27900 - 0
            // too high 44300 - puzzle.part1Score
            throw IllegalArgumentException("Didn't find a new score for $index $puzzle")
        }.sum()
    }

    check(part2(readInput("2023/Day13_Test")) == 400)
    part2(input).println()
}
