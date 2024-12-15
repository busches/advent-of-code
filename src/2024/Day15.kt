package `2024`

import println
import readInput

fun main() {
    val start = System.currentTimeMillis()

    data class Coordinate(val x: Int, val y: Int) {
        operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
    }

    fun moveDirection(move: Char) = when (move) {
        '^' -> Coordinate(0, -1)
        'v' -> Coordinate(0, 1)
        '<' -> Coordinate(-1, 0)
        '>' -> Coordinate(1, 0)
        else -> throw IllegalStateException("Unrecognized move '$move'")
    }

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

    fun part1(input: List<String>): Int {
        val robotSymbol = '@'
        val boxSymbol = 'O'
        val wallSymbol = '#'
        val freeSpace = '.'

        val rawMap = input.take(input.indexOf(""))
        val rawMoves = input.takeLast(input.size - input.indexOf("") - 1)

        val map = buildMap {
            for (y in rawMap.indices) {
                for (x in rawMap[y].indices) {
                    put(Coordinate(x, y), input[y][x])
                }
            }
        }.toMutableMap()
        val moves = rawMoves.flatMap { it.toList() }.toMutableList()


        var robotPosition = map.filterValues { it == robotSymbol }.keys.first()

        while (moves.isNotEmpty()) {
            val move = moves.removeFirst()
            val attemptedPosition = robotPosition + moveDirection(move)
            val whatsAtThePosition = map[attemptedPosition]
            "Found $whatsAtThePosition".println()
            when (whatsAtThePosition) {
                wallSymbol -> {} // We do nothing
                freeSpace -> {
                    map[attemptedPosition] = robotSymbol
                    map[robotPosition] = freeSpace
                    robotPosition = attemptedPosition

                }
                boxSymbol -> {
                    // Figure out if we can push the box the direction we're going
                    val searchDirection = moveDirection(move)
                    var nextSpot = attemptedPosition + searchDirection
                    do {
                        val whatsAtNextSpot = map[nextSpot]
                        if (whatsAtNextSpot == freeSpace) {
                            // We can put a box here and then move the robot
                            map[nextSpot] = boxSymbol
                            map[robotPosition] = freeSpace
                            map[attemptedPosition] = robotSymbol
                            robotPosition = attemptedPosition
                            break
                        } else {
                            nextSpot += searchDirection
                        }
                    } while (whatsAtNextSpot != wallSymbol)
                }
            }
            "Moving $move".println()
            prettyPrintGrid(rawMap.size, input[0].length, map)
        }


        // calculate the GPS of boxes
        return map
            .filterValues { it == boxSymbol }.keys
            .sumOf { (x, y) -> 100 * y + x }
    }


    fun part2(input: List<String>): Int {
        TODO()
    }

    val smallSampleInput = """
        ########
        #..O.O.#
        ##@.O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########

        <^^>>>vv<v>>v<<
    """.trimIndent().lines()
    val sampleInput = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########
        
        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent().lines()
    check(part1(smallSampleInput) == 2028)
    check(part1(sampleInput) == 10092)

    val input = readInput("2024/Day15")
    part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
