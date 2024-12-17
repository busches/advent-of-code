package `2024`

import println
import readInput
import java.util.PriorityQueue


private data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
}

private enum class Direction(var coordinate: Coordinate) {
    West(Coordinate(-1, 0)),
    North(Coordinate(0, 1)),
    East(Coordinate(1, 0)),
    South(Coordinate(0, -1)),
    ;

    fun clockwise(): Direction = entries[(ordinal + 1) % entries.size]

    fun counterClockwise(): Direction = entries[(ordinal + entries.size - 1) % entries.size]
}

fun main() {
    val start = System.currentTimeMillis()

    data class Move(
        val position: Coordinate,
        val direction: Direction,
        val cost: Long,
        val previousCoordinates: Set<Coordinate>
    )

    fun prettyPrintGrid(
        gridHeight: Int,
        gridWidth: Int,
        map: Map<Coordinate, Char>,
        completedMovePaths: Set<Coordinate> = emptySet()
    ) {
        val prettyGrid = mutableListOf<MutableList<String>>(mutableListOf())
        for (y in 0..<gridHeight) {
            prettyGrid.add(y, mutableListOf())
            for (x in 0..<gridWidth) {
                val coordinate = Coordinate(x, y)
                val value = if (completedMovePaths.contains(coordinate)) "O" else map[coordinate].toString()
                prettyGrid[y].add(x, value)
            }
        }
        prettyGrid.joinToString("\n") { it.joinToString("") }.println()
    }

    fun solve(input: List<String>, part1: Boolean): Long {
        val map = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    put(Coordinate(x, y), input[y][x])
                }
            }
        }


        val startingPosition = map.filterValues { it == 'S' }.keys.first()
        startingPosition.println()
        val endingPosition = map.filterValues { it == 'E' }.keys.first()

        val startingMove = Move(startingPosition, Direction.East, 0, emptySet())

        val remainingMoves = PriorityQueue<Move>(compareBy { move -> move.cost })
        remainingMoves.add(startingMove)
        val visited = mutableSetOf<Triple<Coordinate, Direction, Set<Coordinate>>>()
        var lowestCost = Long.MAX_VALUE

        val completedMovePaths = mutableSetOf<Coordinate>()
        while (remainingMoves.isNotEmpty()) {
            val move = remainingMoves.remove()
//            if (move.position == Coordinate(x=5, y=7) && move.direction == Direction.East) {
//                "holy shit we're here - $move".println()
//                prettyPrintGrid(input.size, input[0].length, map, move.previousCoordinates.toSet() + move.position)
//            }

            if (move.cost > lowestCost) {
                continue // no reason to keep going
            }
            val whatsHere = map[move.position]
            if (whatsHere == null || whatsHere == '#') {
                continue // can't walk off the grid or into a wall
            }
            if (move.position == endingPosition) {
                if (part1) {
                    return move.cost
                } else {
                    "Found an exit path! - ${move.cost}".println()
                    if (move.cost <= lowestCost) {
                        "added the path".println()
                        lowestCost = move.cost
                        completedMovePaths.addAll(move.previousCoordinates + move.position)
                    }
                    continue
                }
            }
            if (!visited.add(Triple(move.position, move.direction, move.previousCoordinates))) {
                continue // already been here
            }
            // We can only go straight, left, or right, never back
            val currentDirection = move.direction
            remainingMoves.add(
                move.copy(
                    position = move.position + currentDirection.coordinate,
                    cost = move.cost + 1,
                    previousCoordinates = move.previousCoordinates + move.position
                )
            )
            remainingMoves.add(
                move.copy(
                    position = move.position,
                    cost = move.cost + 1000,
                    direction = currentDirection.counterClockwise(),
                    previousCoordinates = move.previousCoordinates + move.position
                )
            )
            remainingMoves.add(
                move.copy(
                    position = move.position,
                    cost = move.cost + 1000,
                    direction = currentDirection.clockwise(),
                    previousCoordinates = move.previousCoordinates + move.position
                )
            )
        }

        prettyPrintGrid(input.size, input[0].length, map, completedMovePaths)
        return completedMovePaths.size.toLong().also { it.println() }
    }

    fun part1(input: List<String>): Long {
        return solve(input, true)
    }


    fun part2(input: List<String>): Long {
        return solve(input, false)
    }

    val sampleInput = """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
    """.trimIndent().lines()
//    check(part1(sampleInput) == 7036L)

    val input = readInput("2024/Day16")
//    part1(input).println()

    check(part2(sampleInput) == 45L)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
