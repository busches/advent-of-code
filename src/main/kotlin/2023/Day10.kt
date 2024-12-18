package `2023`

import utils.println
import utils.readInput

enum class Direction(val changeY: Int, val changeX: Int) {
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

    val path = mutableListOf<Pair<Int, Int>>()

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toList() }

        var steps = 1
        for ((y, line) in input.withIndex()) {
            for ((x, rawPipe) in line.withIndex()) {
                if (rawPipe != 'S') continue

                val startingLocation = y to x
                path += startingLocation
                directionSearch@ for (direction: Direction in Direction.entries) {
                    val nextLocation = startingLocation.move(direction)
                    var nextPipe = getPipe(grid, nextLocation)
                    val oppositeDirection = direction.opposite()
                    if (nextPipe == Pipe.NOT_A_PIPE || !nextPipe.directions.contains(oppositeDirection)) {
                        continue@directionSearch
                    }

                    path += nextLocation
                    var nextDirection = direction
                    var currentLocation = nextLocation
                    while (nextPipe != Pipe.START) {
                        val oppositeDirection = nextDirection.opposite()
                        nextDirection = nextPipe.directions.first { it != oppositeDirection }
                        currentLocation = currentLocation.move(nextDirection)
                        nextPipe = getPipe(grid, currentLocation)
                        path += currentLocation
                        steps += 1
                    }
                    break
                }
            }
        }
        "Total Steps $steps".println()
        return steps / 2
    }

    check(part1(readInput("2023/Day10_Test")) == 8)

    val input = readInput("2023/Day10")
    part1(input).println()

    fun part2(input: List<String>): Int {
        // Use part1 to give us the path of our steps
        path.clear() // RESET FOR THE TESTS
        part1(input)

        val grid = input.map { it.toList() }
        val start = path.first()

        // Use Ray Casting to see if how many times we cross the original grid
        // If odd number of times, then the point was in the grid, if it's even, we were outside the grid
        // https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
        var pointsInsideTheGrid = 0
        for ((y, line) in input.withIndex()) {
            for ((x, _) in line.withIndex()) {
                if (y to x in path) { // Ignore points that are in the path
                    continue
                }

                // Loop from current point's x to edge of grid
                var intersections = 0
                for (offset in 0..<(line.length - x)) {
                    val currentCoordinate = y to x + offset
                    if (currentCoordinate in path) {
                        // Simulate we're shooting offset a bit, so we ignore -, L, J as intersections, otherwise we count edges too many times
                        val pipe = getPipe(grid, currentCoordinate)
                        if (pipe !in listOf(Pipe.HORIZONTAL, Pipe.L_BEND, Pipe.J_BEND)) {
                            intersections++
                        }
                    }
                }
                if (intersections % 2 == 1) {
                    pointsInsideTheGrid += 1
                }
            }
        }
        return pointsInsideTheGrid
    }

    check(part2(readInput("2023/Day10_Test2")) == 8)
    check(part2(readInput("2023/Day10_Test3")) == 4)
    check(part2(readInput("2023/Day10_Test4")) == 10)
    part2(input).println()
}

private fun Pair<Int, Int>.move(direction: Direction): Pair<Int, Int> =
    this.first + direction.changeY to this.second + direction.changeX
