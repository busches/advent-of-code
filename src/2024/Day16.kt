package `2024`

import println
import readInput
import java.util.PriorityQueue


@JvmInline
private value class Coordinate(val coordinates: Pair<Int, Int>) {
    operator fun plus(other: Coordinate) =
        Coordinate(coordinates.first + other.coordinates.first to coordinates.second + other.coordinates.second)
}

private enum class Direction(var coordinate: Coordinate) {
    West(Coordinate(-1 to 0)),
    North(Coordinate(0 to 1)),
    East(Coordinate(1 to 0)),
    South(Coordinate(0 to -1)),
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
    ) {
        fun getNextMoves() = listOf(
            // We can only go straight, left, or right, never back
            copy(
                position = position + direction.coordinate,
                cost = cost + 1,
            ),
            copy(
                position = position,
                cost = cost + 1000,
                direction = direction.counterClockwise(),
            ),
            copy(
                position = position,
                cost = cost + 1000,
                direction = direction.clockwise(),
            )
        )
    }

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
                val coordinate = Coordinate(x to y)
                val value = if (completedMovePaths.contains(coordinate)) "O" else map[coordinate].toString()
                prettyGrid[y].add(x, value)
            }
        }
        prettyGrid.joinToString("\n") { it.joinToString("") }.println()
    }

    fun buildMap(input: List<String>): Map<Coordinate, Char> {
        val map = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    put(Coordinate(x to y), input[y][x])
                }
            }
        }
        return map
    }

    fun findLowestScore(
        map: Map<Coordinate, Char>, startingPosition: Coordinate, endingPosition: Coordinate, direction: Direction
    ): Long {
        val startingMove = Move(startingPosition, direction, 0)
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
            if (!visited.add(Pair(move.position, move.direction))) {
                continue // already been here
            }
            remainingMoves.addAll(move.getNextMoves())
        }

        TODO("We're lost in the maze")
    }

    fun getNumberOfWaysThroughMaze(
        startingPosition: Coordinate,
        end: Coordinate,
        targetScore: Long,
        map: Map<Coordinate, Char>
    ): Int {
        val visited = HashSet<Pair<Coordinate, Direction>>()
        val queue = ArrayDeque<Move>()
        val validPositions = HashSet<Coordinate>()

        queue.add(Move(startingPosition, Direction.East, 0))
        while (queue.isNotEmpty()) {
            val move = queue.removeFirst()
            validPositions.add(move.position)

            if (move.position == end) continue

            if (!visited.add(move.position to move.direction)) {
                continue // already been here
            }

            val nextMoves = move.getNextMoves()
            for (nextMove in nextMoves) {
                // Check before we accumulate the memory now
                if (map[nextMove.position] == '#' || visited.contains(nextMove.position to nextMove.direction)) {
                    continue
                }
                // Checking for lowest score here, we effectively only check one entire path at a time
                // instead of slowly accumulating more and more potential paths to check and going OOM
                val findLowestScoreFromHere = findLowestScore(map, nextMove.position, end, nextMove.direction)
                if (nextMove.cost + findLowestScoreFromHere > targetScore) continue

                queue.add(nextMove)
            }
        }

        return validPositions.count()
    }

    fun part1(input: List<String>): Long {
        val map = buildMap(input)
        val startingPosition = map.filterValues { it == 'S' }.keys.first()
        val endingPosition = map.filterValues { it == 'E' }.keys.first()
        return findLowestScore(
            map,
            startingPosition,
            endingPosition,
            Direction.East
        )
    }

    fun part2(input: List<String>): Int {
        val map = buildMap(input)
        val startingPosition = map.filterValues { it == 'S' }.keys.first()
        val endingPosition = map.filterValues { it == 'E' }.keys.first()
        val lowestScore = findLowestScore(map, startingPosition, endingPosition, Direction.East)

        return getNumberOfWaysThroughMaze(startingPosition, endingPosition, lowestScore, map)
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

    check(part2(sampleInput) == 45)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
