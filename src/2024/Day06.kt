package `2024`

import println
import readInput

fun main() {

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

    fun solve(originalMap: List<MutableList<Char>>): Triple<List<MutableList<Char>>, MutableSet<Triple<Int, Int, Char>>, Boolean> {
        val map = originalMap.toMutableList().map { it.toMutableList() }
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
            map[currentPosition.second][currentPosition.first] = 'X'
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
                map[nextPosition.second][nextPosition.first] = facing
                currentPosition = nextPosition
            }
        }
        return Triple(map, visited, looping)
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toMutableList() }
        val solvedMap = solve(map)

        return solvedMap.first.sumOf { line -> line.count { it == 'X' } }
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toMutableList() }
        val firstSolve = solve(map)
        val startingPoint = firstSolve.second.first().let { it.first to it.second }
        val visitedPoints = firstSolve.second
            .map { it.first to it.second }
            .toMutableSet()
            .apply { remove(startingPoint) }

        return visitedPoints.count { point ->
            val alteredMap = map.toMutableList().map { it.toMutableList() }.apply {
                this[point.second][point.first] = '#'
            }
            val solvedMap = solve(alteredMap)
            val looped = solvedMap.third
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
}

private operator fun Pair<Int, Int>.plus(currentPosition: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + currentPosition.first to this.second + currentPosition.second
}
