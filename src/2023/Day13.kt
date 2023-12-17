package `2023`

import println
import readInput
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

    fun part2(input: List<String>): Int {
        TODO()
    }

    check(part2(readInput("2023/Day13_Test")) == 400)
    part2(input).println()
}
