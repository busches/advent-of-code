package `2024`

import utils.println
import utils.readInput
import java.util.*


class Day20 {
    @JvmInline
    value class Coordinate(val coordinates: Pair<Int, Int>) {
        operator fun plus(other: Coordinate) =
            Coordinate(coordinates.first + other.coordinates.first to coordinates.second + other.coordinates.second)
    }

    enum class Direction(var coordinate: Coordinate) {
        West(Coordinate(-1 to 0)),
        North(Coordinate(0 to 1)),
        East(Coordinate(1 to 0)),
        South(Coordinate(0 to -1)),
        ;
    }

    data class Move(
        val position: Coordinate,
        val steps: Int,
        val cheatsLeft: Int = 1,
        val moves: Set<Coordinate> = emptySet()
    ) {
        fun getNextMoves(): Collection<Move> {
            return Direction.entries.map { direction ->
                copy(position = position + direction.coordinate, steps = steps + 1, moves = moves + position)
            }
        }
    }

    private fun buildMap(input: List<String>): Map<Coordinate, Char> {
        val map = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    put(Coordinate(x to y), input[y][x])
                }
            }
        }
        return map
    }

    private fun prettyPrintGrid(
        gridHeight: Int,
        gridWidth: Int,
        map: Map<Coordinate, Char>,
        completedMovePaths: Set<Coordinate> = emptySet()
    ) {
        val prettyGrid = mutableListOf<MutableList<String>>(mutableListOf())
        for (y in 0..<gridHeight) {
            prettyGrid.add(y, mutableListOf())
            for (x in 0..<gridWidth) {
                val coordinate = Coordinate(x to y)
                val value = if (completedMovePaths.contains(coordinate)) "O" else map[coordinate].toString()
                prettyGrid[y].add(x, value)
            }
        }
        prettyGrid.joinToString("\n") { it.joinToString("") }.println()
    }

    private fun solve(
        map: Map<Coordinate, Char>,
        startingPoint: Coordinate,
        endingPoint: Coordinate,
        allowCheating: Boolean,
        fastestSolveWithCheating: Int
    ): List<Int> {
        val remainingMoves = PriorityQueue<Move>(compareBy { move -> if (allowCheating) move.steps * -1 else move.steps })
        remainingMoves.add(Move(startingPoint, 0))

        val finishedRaces = mutableListOf<Int>()

        val visited = mutableSetOf<Pair<Coordinate, Set<Coordinate>>>()
        while (remainingMoves.isNotEmpty()) {
            var move = remainingMoves.remove()
            val whatsHere = map[move.position]
            var cheatedThisTurn = false
            if (move.steps > fastestSolveWithCheating) {
                // No reason to keep trying
                continue
            } else if (move.position in move.moves) {
                // We've already been here
                continue
            } else if (whatsHere == null) {
                continue /// can't walk off the grid
            } else if (whatsHere == '#') {
                if (allowCheating && move.cheatsLeft > 0) {
                    move = move.copy(cheatsLeft = move.cheatsLeft - 1)
                    cheatedThisTurn = true
                } else {
                    continue // can't walk into a wall, unless we're cheating
                }
            }

            // Need to decrease cheating if we're cheating and we didn't cheat this turn
//            if (!cheatedThisTurn && move.cheatsLeft == 1) {
//                move = move.copy(cheatsLeft = 0)
//            }

            if (move.position == endingPoint) {
                finishedRaces.add(move.steps)
                if (!allowCheating) {
//

                    return finishedRaces
                }
            }
            if (!visited.add(move.position to move.moves)) {
                continue // already been here
            }
            remainingMoves.addAll(move.getNextMoves())
        }
        return finishedRaces
    }

    fun part1(input: List<String>, timeSaved: Int = 100): Int {
        val map = buildMap(input)
        val startingPoint = map.filterValues { it == 'S' }.keys.first()
        val endingPoint = map.filterValues { it == 'E' }.keys.first()

        val fastestSolveWithoutCheating = solve(map, startingPoint, endingPoint, false, Int.MAX_VALUE)
            .first()


        // Sample input should be 84
        "Fastest solve without cheat $fastestSolveWithoutCheating".println()

//        TODO()
//        prettyPrintGrid(15, 15, map, move.moves)

        val allSolves = solve(map, startingPoint, endingPoint, true, fastestSolveWithoutCheating)

        return allSolves
            .filter { fastestSolveWithoutCheating - it >= timeSaved }
//            .onEach { grid ->
//                "Here's a grid".println()
//                prettyPrintGrid(15, 15, map, grid)
//            }
            .count()
    }
}

fun main() {
    val start = System.currentTimeMillis()


    val day20 = Day20()


    fun part2(input: List<String>): Int {
        TODO("Need to implement Part 2")
    }

    val sampleInput = """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
    """.trimIndent().lines()

//    check(day20.part1(sampleInput, 64) == 1)
//    check(day20.part1(sampleInput, 40) == 2)
//    check(day20.part1(sampleInput, 38) == 3)
//    check(day20.part1(sampleInput, 36) == 4)
//    check(day20.part1(sampleInput, 20) == 5)
    check(day20.part1(sampleInput, 12) == 8)

    val input = readInput("2024/Day20")
    day20.part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
