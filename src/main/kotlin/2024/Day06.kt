package `2024`

import utils.println
import utils.readInput

fun main() {
    val start = System.currentTimeMillis()

    fun nextDirection(facing: Char): Pair<Int, Int> {
        return when (facing) {
            '>' -> 1 to 0
            '<' -> -1 to 0
            '^' -> 0 to -1
            'v' -> 0 to 1
            else -> throw IllegalArgumentException("what -$facing")
        }
    }

    fun turnRight(facing: Char): Char {
        return when (facing) {
            '>' -> 'v'
            '<' -> '^'
            '^' -> '>'
            'v' -> '<'
            else -> throw IllegalArgumentException("what -$facing")
        }
    }

    fun solve(map: List<List<Char>>): Pair<Set<Triple<Int, Int, Char>>, Boolean> {
        var currentPosition = -1 to -1
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] == '^') {
                    currentPosition = x to y
                    break
                }
            }
        }

        var facing = '^'
        val visited = mutableSetOf<Triple<Int, Int, Char>>()
        var looping = false
        while (true) {
            val nextPosition = currentPosition + nextDirection(facing)
            if (!visited.add(Triple(currentPosition.first, currentPosition.second, facing))) {
                // We're looping as we've been here before facing the same way
                looping = true
                break
            }
            val inBounds = nextPosition.first in 0..map[0].lastIndex && nextPosition.second in 0..map.lastIndex
            if (!inBounds) {
                break
            }
            val nextPositionBlocked =
                map[nextPosition.second][nextPosition.first] == '#'
            if (nextPositionBlocked) {
                facing = turnRight(facing)
            } else {
                currentPosition = nextPosition
            }
        }
        return visited to looping
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toMutableList() }
        val (visited, _) = solve(map)

        return visited.map { (x, y, _) -> x to y }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toMutableList() }
        // Solve it once so we only add obstacles to visited places
        val (visited, _) = solve(map)
        val startingPoint = visited.first().let { (x, y, _) -> x to y }
        val visitedPoints = visited
            .map { (x, y, _) -> x to y }
            .toMutableSet()
            .apply { remove(startingPoint) }

        return visitedPoints.count { (x, y) ->
            val alteredMap = map.toMutableList()
                .map { it.toMutableList() }
                .apply { this[y][x] = '#' }
            val (_, looped) = solve(alteredMap)
            looped
        }
    }

    val sampleInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()
    check(part1(sampleInput) == 41)

    val input = readInput("2024/Day06")
    part1(input).println()

    check(part2(sampleInput) == 6)
    part2(input).println()
    "Ran in ${(System.currentTimeMillis() - start)} milliseconds".println()
}

private operator fun Pair<Int, Int>.plus(currentPosition: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + currentPosition.first to this.second + currentPosition.second
}
