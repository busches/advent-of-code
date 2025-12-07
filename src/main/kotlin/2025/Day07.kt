package `2025`

import utils.println
import utils.readInput
import utils.transpose

fun main() {
    data class Coordinate(val x: Int, val y: Int) {
        fun moveDown(): Coordinate {
            return Coordinate(x, y + 1)
        }

        fun splitLeft(): Coordinate {
            return Coordinate(x - 1, y)
        }

        fun splitRight(): Coordinate {
            return Coordinate(x + 1, y)
        }
    }

    fun part1(input: List<String>): Long {
        val map = buildMap {
            for (y in input.indices) {
                for (x in input[y].indices) {
                    put(Coordinate(x, y), input[y][x])
                }
            }
        }
        val start = map.firstNotNullOf { entry -> entry.value.takeIf { it == 'S' }?.let { entry.key } }

        var splits = 0L
        val beams = ArrayDeque<Coordinate>()
        beams.add(start.moveDown())
        
        val beamsStarted = HashSet<Coordinate>().apply { add(start) }

        while (beams.isNotEmpty()) {
            val currentBeam = beams.removeFirst()
            val whatsHere = map[currentBeam]
            when (whatsHere) {
                '.' -> {
                    val newBeam = currentBeam.moveDown()
                    if (beamsStarted.add(newBeam)) {
                        beams.add(newBeam)    
                    }
                    
                }
                '^' -> {
                    val splitLeft = currentBeam.splitLeft()
                    if (beamsStarted.add(splitLeft)) {
                        beams.add(splitLeft)
                    }
                    val splitRight = currentBeam.splitRight()
                    if (beamsStarted.add(splitRight)) {
                        beams.add(splitRight)
                    }
                    splits++
                }
            }
        }


        return splits.also { "Found splits $splits".println() }
    }


    val sampleInput = """
        .......S.......
        ...............
        .......^.......
        ...............
        ......^.^......
        ...............
        .....^.^.^.....
        ...............
        ....^.^...^....
        ...............
        ...^.^...^.^...
        ...............
        ..^...^.....^..
        ...............
        .^.^.^.^.^...^.
        ...............
    """.trimIndent()
    check(part1(sampleInput.lines()) == 21L)


    fun part2(input: List<String>): Long {
        TODO()
    }

    val input = readInput("2025/Day07")
    part1(input).println()

    check(part2(sampleInput.lines()) == 3263827L)

    part2(input).println()
}
