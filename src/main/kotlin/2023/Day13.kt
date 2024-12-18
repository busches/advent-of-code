package `2023`

import utils.println
import utils.readInput
import utils.transpose

fun main() {

    data class Puzzle(val rows: List<String>) {
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
        for (index in 0..(newPuzzle.size - 2)) {
            if (newPuzzle[index] == newPuzzle[index + 1]) {
                for (nextRowCounter in 1..<(newPuzzle.size - index)) {
                    val topRow = index - nextRowCounter
                    val bottomRow = index + 1 + nextRowCounter
                    if (topRow !in newPuzzle.indices || bottomRow !in newPuzzle.indices) {
                        return index + 1
                    } else {
                        if (newPuzzle[topRow] != newPuzzle[bottomRow]) {
                            break
                        }
                    }
                }
            }
        }
        return 0
    }

    fun scorePuzzle(puzzle: Puzzle): Int {
        var score = getReflectionLine(puzzle.rows) * 100
        if (score == 0) {
            val verticalPuzzle = puzzle.rows.map { it.toList() }.transpose().map { it.joinToString("") }
            score = getReflectionLine(verticalPuzzle)
        }
        return score
    }

    fun part1(input: List<String>): Int {
        return extractPuzzles(input).sumOf { puzzle -> scorePuzzle(puzzle) }
    }

    check(part1(readInput("2023/Day13_Test")) == 405)

    val input = readInput("2023/Day13")
    part1(input).println()

    fun smudgeRow(puzzle: List<List<Char>>): Int {
        val reversedPuzzle = puzzle.asReversed()
        for (index in 1..<puzzle.size) {
            val above = reversedPuzzle.takeLast(index)
            val below = puzzle.drop(index)

            // Zip our rows together, then compare each character to see if we only have 1 difference
            // If there's only one difference, that's the smudge apparently, even if the next set of rows don't match
            val differenceCount = above
                .zip(below)
                .sumOf { (aboveRow, belowRow) ->
                    aboveRow
                        .zip(belowRow)
                        .count { (aboveChar, belowChar) -> aboveChar != belowChar }
                }
            if (differenceCount == 1) {
                return index
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val puzzles = extractPuzzles(input).map { it.rows.map { it.toList() } }
        val horizontalRowsWithSmudge = puzzles.sumOf { puzzle -> smudgeRow(puzzle) }  * 100

        val transposed = puzzles.map { it.transpose() }
        val verticalRowsBeforeSmudge = transposed.sumOf { puzzle -> smudgeRow(puzzle) }

        return (verticalRowsBeforeSmudge + horizontalRowsWithSmudge)
    }

    check(part2(readInput("2023/Day13_Test")) == 400)
    part2(input).println()
}
