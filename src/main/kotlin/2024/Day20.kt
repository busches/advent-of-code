package `2024`

import utils.println
import utils.readInput
import java.util.*
import kotlin.math.absoluteValue


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
        val moves: List<Coordinate> = emptyList()
    ) {
        fun getNextMoves(): Collection<Move> {
            return Direction.entries.map { direction ->
                copy(position = position + direction.coordinate, steps = steps + 1, moves = moves + position)
            }
        }

        // Cheat by potentially stepping over a wall
        fun getCheatMoves(): Collection<Move> {
            return Direction.entries.map { direction ->
                val firstPosition = position + direction.coordinate
                val secondPosition = firstPosition + direction.coordinate
                copy(position = secondPosition, steps = steps + 2, moves = moves + position + firstPosition)
            }
        }

        private val totalCheats = 20

        // Now we're really cheating
        fun getMegaCheatMoves(): Collection<Move> {
            return (-totalCheats..totalCheats).flatMap { xOffset ->
                (-totalCheats..totalCheats).mapNotNull { yOffset ->
                    if (xOffset == 0 && yOffset == 0) {
                        null // skip doing nothing
                    } else {
                        val cheats = xOffset.absoluteValue + yOffset.absoluteValue
                        if (cheats > totalCheats) {
                            // can't cheat this much
                            null
                        } else {
                            val newPosition =
                                Coordinate(position.coordinates.first + xOffset to position.coordinates.second + yOffset)
                            copy(
                                position = newPosition,
                                steps = steps + cheats
                            ) // We don't have the moves anymore, so we split solve into two gross methods
                        }

                    }
                }
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

    private fun solvePath(
        map: Map<Coordinate, Char>,
        startingMove: Move,
        endingPoint: Coordinate,
        fastestSolveWithCheating: Int
    ): List<Coordinate>? {
        val remainingMoves = PriorityQueue<Move>(compareBy { move -> move.steps })
        remainingMoves.add(startingMove)

        val visited = mutableSetOf<Coordinate>()
        while (remainingMoves.isNotEmpty()) {
            val move = remainingMoves.remove()
            val whatsHere = map[move.position]
            if (move.steps > fastestSolveWithCheating) {
                // No reason to keep trying
                return null
            } else if (move.position in move.moves) {
                // We've already been here
                continue
            } else if (whatsHere == null) {
                continue /// can't walk off the grid
            } else if (whatsHere == '#') {
                continue // can't walk into a wall
            } else if (move.position == endingPoint) {
                return move.moves
            } else if (!visited.add(move.position)) {
                continue // already been here
            }
            remainingMoves.addAll(move.getNextMoves())
        }
        return null
    }

    private fun solveSteps(
        map: Map<Coordinate, Char>,
        startingMove: Move,
        endingPoint: Coordinate,
        fastestSolveWithCheating: Int
    ): Int? {
        val remainingMoves = PriorityQueue<Move>(compareBy { move -> move.steps })
        remainingMoves.add(startingMove)

        val visited = mutableSetOf<Coordinate>()
        while (remainingMoves.isNotEmpty()) {
            val move = remainingMoves.remove()
            val whatsHere = map[move.position]
            if (move.steps > fastestSolveWithCheating) {
                // No reason to keep trying
                return null
            } else if (move.position in move.moves) {
                // We've already been here
                continue
            } else if (whatsHere == null) {
                continue /// can't walk off the grid
            } else if (whatsHere == '#') {
                continue // can't walk into a wall
            } else if (move.position == endingPoint) {
                return move.steps
            } else if (!visited.add(move.position)) {
                continue // already been here
            }
            remainingMoves.addAll(move.getNextMoves())
        }
        return null
    }

    fun part1(input: List<String>, timeSaved: Int = 100): Int {
        val map = buildMap(input)
        val startingPoint = map.filterValues { it == 'S' }.keys.first()
        val endingPoint = map.filterValues { it == 'E' }.keys.first()

        val fastestPathWithoutCheating = solvePath(map, Move(startingPoint, 0), endingPoint, Int.MAX_VALUE)!!
        val fastestTimeWithoutCheating = fastestPathWithoutCheating.size

        // Sample input should be 84
        "Fastest solve without cheat $fastestTimeWithoutCheating".println()

//        prettyPrintGrid(15, 15, map, move.moves)

        // Path is well-defined, so we only need to cheat along the path
        val newCheatingStartPositions = fastestPathWithoutCheating.flatMapIndexed { index, coordinate ->
            val recreatedMove = Move(coordinate, index, moves = fastestPathWithoutCheating.take(index))
            recreatedMove.getCheatMoves()
        }

        val timingCache = fastestPathWithoutCheating
            .mapIndexed { index, coordinate -> coordinate to index }
            .toMap(mutableMapOf())

        val allSolves = newCheatingStartPositions
            .mapNotNull { startingMove ->
                if (timingCache.containsKey(startingMove.position)) {
                    fastestTimeWithoutCheating - timingCache[startingMove.position]!! + startingMove.steps
                } else {
                    solveSteps(map, startingMove, endingPoint, fastestTimeWithoutCheating)
                }
            }

        return allSolves.count { fastestTimeWithoutCheating - it >= timeSaved }
    }

    fun part2(input: List<String>, timeSaved: Int = 100): Int {
        val map = buildMap(input)
        val startingPoint = map.filterValues { it == 'S' }.keys.first()
        val endingPoint = map.filterValues { it == 'E' }.keys.first()

        val fastestPathWithoutCheating = solvePath(map, Move(startingPoint, 0), endingPoint, Int.MAX_VALUE)!!
        val fastestTimeWithoutCheating = fastestPathWithoutCheating.size

        // Sample input should be 84
        "Fastest solve without cheat $fastestTimeWithoutCheating".println()

//        prettyPrintGrid(15, 15, map, move.moves)

        // Path is well-defined, so we only need to cheat along the path
        val newCheatingStartPositions = fastestPathWithoutCheating.flatMapIndexed { index, coordinate ->
            val recreatedMove = Move(coordinate, index, moves = fastestPathWithoutCheating.take(index))
            recreatedMove.getMegaCheatMoves()
        }

        // This is not as good for part 2, since we cheat a lot more, but still worked in <2 minutes
        val timingCache = fastestPathWithoutCheating
            .mapIndexed { index, coordinate -> coordinate to index }
            .toMap(mutableMapOf())

        val allSolves = newCheatingStartPositions
            .mapNotNull { startingMove ->
                if (timingCache.containsKey(startingMove.position)) {
                    fastestTimeWithoutCheating - timingCache[startingMove.position]!! + startingMove.steps
                } else {
                    solveSteps(map, startingMove, endingPoint, fastestTimeWithoutCheating)
                }
            }

        return allSolves.count { fastestTimeWithoutCheating - it >= timeSaved }
    }
}

fun main() {
    val start = System.currentTimeMillis()


    val day20 = Day20()

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

    check(day20.part1(sampleInput, 64) == 1)
    check(day20.part1(sampleInput, 40) == 2)
    check(day20.part1(sampleInput, 38) == 3)
    check(day20.part1(sampleInput, 36) == 4)
    check(day20.part1(sampleInput, 20) == 5)
    check(day20.part1(sampleInput, 12) == 8)

    val input = readInput("2024/Day20")
    day20.part1(input).println()

    check(day20.part2(sampleInput, 76) == 3)
    day20.part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
