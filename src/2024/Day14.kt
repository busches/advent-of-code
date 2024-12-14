package `2024`

import println
import readInput

private data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
}

private data class Robot(val position: Coordinate, val movement: Coordinate) {
    fun move(gridWidth: Int, gridHeight: Int): Robot {
        var (x, y) = position + movement
        if (x >= gridWidth) {
            x -= gridWidth
        }
        if (x < 0) {
            x += gridWidth
        }
        if (y >= gridHeight) {
            y -= gridHeight
        }
        if (y < 0) {
            y += gridHeight
        }

        return copy(position = Coordinate(x, y))
    }
}

fun main() {
    val start = System.currentTimeMillis()

    fun prettyPrintGrid(gridHeight: Int, gridWidth: Int, robots: List<Robot>) {
        val prettyGrid = mutableListOf<MutableList<String>>(mutableListOf())
        for (y in 0..<gridHeight) {
            prettyGrid.add(y, mutableListOf())
            for (x in 0..<gridWidth) {
                prettyGrid[y].add(
                    x,
                    robots.count { it.position.x == x && it.position.y == y }
                        .let { if (it == 0) "." else it.toString() })
            }
        }
        prettyGrid.joinToString("\n") { it.joinToString("") }.println()
    }

    fun solve(input: List<String>, gridWidth: Int, gridHeight: Int, seconds: Int): Long {
        var robots = input.map { line ->
            val (position, velocity) = line.replace("p=", "").replace("v=", "").split(" ")
            val (positionX, positionY) = position.split(",").map { it.toInt() }
            val (velocityX, velocityY) = velocity.split(",").map { it.toInt() }

            Robot(
                position = Coordinate(positionX, positionY),
                movement = Coordinate(velocityX, velocityY)
            )
        }

        repeat(seconds) { moveNumber ->
            robots = robots.map {
                it.move(gridWidth, gridHeight)
            }

            if (seconds != 100) {
                "Move number ${moveNumber + 1}".println()
                prettyPrintGrid(gridHeight, gridWidth, robots)
            }
        }

        val widthMiddle = gridWidth / 2
        val heightMiddle = gridHeight / 2 // 3.5 - row 4 (which is index 3)

        val quadrants = robots.fold(mutableMapOf<Pair<Boolean, Boolean>, Int>()) { acc, robot ->
            if (robot.position.x == widthMiddle || robot.position.y == heightMiddle) {
                // doesn't count
                acc
            } else {
                val quadrant = if (robot.position.x > widthMiddle) {
                    if (robot.position.y > heightMiddle) {
                        true to true
                    } else {
                        true to false
                    }
                } else if (robot.position.y > heightMiddle) {
                    false to true
                } else {
                    false to false
                }
                val quadrantCount = acc.getOrDefault(quadrant, 0)
                acc[quadrant] = quadrantCount + 1
                acc
            }
        }
        return quadrants.values.fold(1L) { total, count -> total * count }
    }

    fun part1(input: List<String>, gridWidth: Int = 101, gridHeight: Int = 103): Long {
        return solve(input, gridWidth, gridHeight, 100)
    }


    // This puzzle - wat
    fun part2(input: List<String>): Long {
        return solve(input, 101, 103, 10000)
    }

    val sampleInput = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent().lines()
    check(part1(sampleInput, 11, 7) == 12L)

    val input = readInput("2024/Day14")
    part1(input).println()

    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
