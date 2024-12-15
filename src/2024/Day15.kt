package `2024`

import println
import readInput

fun main() {
    val start = System.currentTimeMillis()

    data class Coordinate(val x: Int, val y: Int) {
        operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
        operator fun minus(other: Coordinate) = Coordinate((x - other.x), (y - other.y))
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

    val robotSymbol = '@'
    val boxSymbol = 'O'
    val wallSymbol = '#'
    val freeSpace = '.'
    val leftBoxSymbol = '['
    val rightBoxSymbol = ']'

    fun part1(input: List<String>): Int {
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
//            prettyPrintGrid(rawMap.size, input[0].length, map)
        }

        // calculate the GPS of boxes
        return map
            .filterValues { it == boxSymbol }.keys
            .sumOf { (x, y) -> 100 * y + x }
    }


    fun part2(input: List<String>): Int {
        val rawMap = input.take(input.indexOf(""))
        val rawMoves = input.takeLast(input.size - input.indexOf("") - 1)

        // Map is 2x as wide as part 1
        val map = buildMap {
            for (y in rawMap.indices) {
                var nextX = 0
                for (x in rawMap[y].indices) {
                    val value = input[y][x]
                    when (value) {
                        wallSymbol, freeSpace -> {
                            put(Coordinate(nextX, y), value)
                            put(Coordinate(nextX + 1, y), value)
                        }

                        robotSymbol -> {
                            put(Coordinate(nextX, y), value)
                            put(Coordinate(nextX + 1, y), freeSpace)
                        }

                        boxSymbol -> {
                            put(Coordinate(nextX, y), leftBoxSymbol)
                            put(Coordinate(nextX + 1, y), rightBoxSymbol)
                        }
                    }
                    nextX += 2
                }
            }
        }.toMutableMap()

        prettyPrintGrid(rawMap.size, input[0].length * 2, map)

        val moves = rawMoves.flatMap { it.toList() }.toMutableList()
        var robotPosition = map.filterValues { it == robotSymbol }.keys.first()

        while (moves.isNotEmpty()) {
            val move = moves.removeFirst()
            "Moving $move".println()
            val attemptedPosition = robotPosition + moveDirection(move)
            val whatsAtThePosition = map[attemptedPosition]
            when (whatsAtThePosition) {
                wallSymbol -> {} // We do nothing
                freeSpace -> {
                    map[attemptedPosition] = robotSymbol
                    map[robotPosition] = freeSpace
                    robotPosition = attemptedPosition
                }

                leftBoxSymbol, rightBoxSymbol -> {
                    // Figure out if we can push the box the direction we're going, all boxes get shifted though
                    val searchDirection = moveDirection(move)
                    var nextSpot = attemptedPosition + searchDirection
                    movementLoop@ do {
                        val whatsAtNextSpot = map[nextSpot]
                        if (whatsAtNextSpot == freeSpace) {
                            when (move) {
                                '<' -> {
                                    // Shift everything left
                                    val shifts = (attemptedPosition - nextSpot).x
                                    repeat(shifts) { shiftNumber ->
                                        map[Coordinate(nextSpot.x + shiftNumber, nextSpot.y)] =
                                            if (shiftNumber % 2 == 0) '[' else ']'
                                    }
                                }

                                '>' -> {
                                    // shift everything right
                                    val shifts = (nextSpot - attemptedPosition).x
                                    repeat(shifts) { shiftNumber ->
                                        map[Coordinate(nextSpot.x - shiftNumber, nextSpot.y)] =
                                            if (shiftNumber % 2 == 0) ']' else '['
                                    }
                                }

                                '^' -> {
                                    // this is hard, as we need to calculate all shifts of all boxes we may move and ensure they are valid then perform the move
                                    var allBoxSpaces = listOf(robotPosition) // seed with the robot to start there, even though he is not a box
                                    val spacesToMove = mutableListOf<Coordinate>()

                                    while (true) {
                                        val isThereAWall = allBoxSpaces.any { map[it + Coordinate(0, -1)] == wallSymbol }
                                        if (isThereAWall) {
                                            break@movementLoop
                                        }

                                        spacesToMove.addAll(allBoxSpaces)
                                        val allSpacesAreFree = allBoxSpaces.all { map[it + Coordinate(0, -1)] == freeSpace }
                                        if (allSpacesAreFree) {
                                            // Then we can do the shift of all spaces
                                            break
                                        }
                                        allBoxSpaces = allBoxSpaces
                                            // Shift all the current objects we have north
                                            .map { it + Coordinate(0, -1) }
                                            .filter { map[it] == leftBoxSymbol || map[it] == rightBoxSymbol } // We need to swap the boxes to be two squares
                                            .flatMap { space ->
                                                when (map[space]) {
                                                    leftBoxSymbol -> listOf(space, space + Coordinate(1, 0))
                                                    rightBoxSymbol -> listOf(space + Coordinate(-1, 0), space)
                                                    else -> throw IllegalArgumentException("Something has gone wrong")
                                                }
                                            }
                                            .distinct()
                                    }

                                    // Drop the robot, we'll move him later
                                    spacesToMove.removeFirst()
                                    "These are all the squares we need to move $spacesToMove".println()
                                    // Reverse the list so we can move without losing the history
                                    spacesToMove.reversed().forEach { space ->
                                        map[space + Coordinate(0, -1)] = map[space]!!
                                        map[space] = freeSpace
                                    }
                                }
                                'v' -> {
                                    // this is hard, as we need to calculate all shifts of all boxes we may move and ensure they are valid then perform the move
                                    var allBoxSpaces = listOf(robotPosition) // seed with the robot to start there, even though he is not a box
                                    val spacesToMove = mutableListOf<Coordinate>()

                                    while (true) {
                                        val isThereAWall = allBoxSpaces.any { map[it + Coordinate(0, 1)] == wallSymbol }
                                        if (isThereAWall) {
                                            break@movementLoop
                                        }

                                        spacesToMove.addAll(allBoxSpaces)
                                        val allSpacesAreFree = allBoxSpaces.all { map[it + Coordinate(0, 1)] == freeSpace }
                                        if (allSpacesAreFree) {
                                            // Then we can do the shift of all spaces
                                            break
                                        }
                                        allBoxSpaces = allBoxSpaces
                                            // Shift all the current objects we have north
                                            .map { it + Coordinate(0, 1) }
                                            .filter { map[it] == leftBoxSymbol || map[it] == rightBoxSymbol } // We need to swap the boxes to be two squares
                                            .flatMap { space ->
                                                when (map[space]) {
                                                    leftBoxSymbol -> listOf(space, space + Coordinate(1, 0))
                                                    rightBoxSymbol -> listOf(space + Coordinate(-1, 0), space)
                                                    else -> throw IllegalArgumentException("Something has gone wrong")
                                                }
                                            }
                                            .distinct()
                                    }

                                    // Drop the robot, we'll move him later
                                    spacesToMove.removeFirst()
                                    "These are all the squares we need to move $spacesToMove".println()
                                    // Reverse the list so we can move without losing the history
                                    spacesToMove.reversed().forEach { space ->
                                        map[space + Coordinate(0, 1)] = map[space]!!
                                        map[space] = freeSpace
                                    }
                                }
                            }
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
            prettyPrintGrid(rawMap.size, input[0].length * 2, map)
        }

        // calculate the GPS of boxes
        return map
            .filterValues { it == leftBoxSymbol }.keys
            .sumOf { (x, y) -> 100 * y + x }
            .also { it.println() }

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

    val samplePart2 = """
        #######
        #...#.#
        #.....#
        #..OO@#
        #..O..#
        #.....#
        #######

        <vv<<^^<<^^
    """.trimIndent().lines()

//    check(part2(samplePart2) == 982)
    check(part2(sampleInput) == 9021)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
