package `2023`

import println
import readInput


enum class Direction(val changeX: Int, val changeY: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    fun opposite(): Direction = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
    }
}

enum class Pipe(val character: Char, val directions: List<Direction>) {
    VERTICAL('|', listOf(Direction.UP, Direction.DOWN)),
    HORIZONTAL('-', listOf(Direction.LEFT, Direction.RIGHT)),
    L_BEND('L', listOf(Direction.UP, Direction.RIGHT)),
    J_BEND('J', listOf(Direction.UP, Direction.LEFT)),
    SEVEN_BEND('7', listOf(Direction.DOWN, Direction.LEFT)),
    F_BEND('F', listOf(Direction.DOWN, Direction.RIGHT)),
    NOT_A_PIPE('.', emptyList()),
    START('S', emptyList()),
}

fun main() {

    fun getPipe(grid: List<List<Char>>, nextLocation: Pair<Int, Int>): Pipe {
        val (x, y) = nextLocation
        if (x == -1 || y == -1) {
            return Pipe.NOT_A_PIPE
        }

        val char = grid[x][y]
        return Pipe.entries.first { pipe ->
            pipe.character == char
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }

        var steps = 1
        for ((x, line) in input.withIndex()) {
            for ((y, rawPipe) in line.withIndex()) {
                if (rawPipe != 'S') continue

                val startingLocation = x to y
//                "Starting Location $startingLocation".println()
                directionSearch@ for (direction: Direction in Direction.entries) {
                    val nextLocation = startingLocation.move(direction)
                    var nextPipe = getPipe(grid, nextLocation)
                    val oppositeDirection = direction.opposite()
                    if (nextPipe == Pipe.NOT_A_PIPE || !nextPipe.directions.contains(oppositeDirection)) {
                        continue@directionSearch
                    }

                    var nextDirection = direction
                    var currentLocation = nextLocation
                    while (nextPipe != Pipe.START) {
                        val oppositeDirection = nextDirection.opposite()
                        nextDirection = nextPipe.directions.first { it != oppositeDirection }
                        currentLocation = currentLocation.move(nextDirection)
                        nextPipe = getPipe(grid, currentLocation)
                        steps += 1
//                        "$nextDirection $nextPipe at $currentLocation".println()
                    }
                    break
                }
            }
        }
        "Total Steps $steps".println()
        return steps / 2
    }

//    check(part1(readInput("2023/Day10_Test")) == 8)

    val input = readInput("2023/Day10")
    part1(input).println()

    fun part2(input: List<String>): Int {
        TODO()
    }

    check(part2(readInput("2023/Day10_Test")) == 2)
    part2(input).println()
}

private fun Pair<Int, Int>.move(direction: Direction): Pair<Int, Int> =
    this.first + direction.changeX to this.second + direction.changeY
