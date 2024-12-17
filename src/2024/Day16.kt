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

    data class Move(val position: Coordinate, val direction: Direction, val cost: Long)

    fun prettyPrintGrid(gridHeight: Int, gridWidth: Int, map: Map<Coordinate, Char>) {
        val prettyGrid = mutableListOf<MutableList<String>>(mutableListOf())
        for (y in 0..<gridHeight) {
            prettyGrid.add(y, mutableListOf())
            for (x in 0..<gridWidth) {
                prettyGrid[y].add(x, map[Coordinate(x, y)].toString())
            }
        }
        prettyGrid.joinToString("\n") { it.joinToString("") }.println()
    }

    fun part1(input: List<String>): Long {
        val map = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    put(Coordinate(x, y), input[y][x])
                }
            }
        }

//        prettyPrintGrid(input.size, input[0].length, map)

        val startingPosition = map.filterValues { it == 'S' }.keys.first()
        val endingPosition = map.filterValues { it == 'E' }.keys.first()

        val startingMove = Move(startingPosition, Direction.East, 0)

        val remainingMoves = PriorityQueue<Move>(compareBy { move -> move.cost })
        remainingMoves.add(startingMove)
        val visited = mutableSetOf<Pair<Coordinate, Direction>>()

        while (remainingMoves.isNotEmpty()) {
            val move = remainingMoves.remove()
            val whatsHere = map[move.position]
            if (whatsHere == null || whatsHere == '#') {
                continue // can't walk off the grid or into a wall
            }
            if (move.position == endingPosition) {
                return move.cost
            }
            if (!visited.add(move.position to move.direction)) {
                continue // already been here
            }
            // We can only go straight, left, or right, never back
            val currentDirection = move.direction
            remainingMoves.add(move.copy(position = move.position + currentDirection.coordinate, cost = move.cost + 1))
            remainingMoves.add(
                move.copy(
                    position = move.position,
                    cost = move.cost + 1000,
                    direction = currentDirection.counterClockwise()
                )
            )
            remainingMoves.add(
                move.copy(
                    position = move.position,
                    cost = move.cost + 1000,
                    direction = currentDirection.clockwise()
                )
            )
        }

        TODO("We didn't find the exit")
    }


    fun part2(input: List<String>): Int {
        TODO()
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
    check(part1(sampleInput) == 7036L)

    val input = readInput("2024/Day16")
    part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
