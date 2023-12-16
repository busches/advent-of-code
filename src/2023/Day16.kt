package `2023`

import println
import readInput

fun main() {

    data class BeamDirection(val coords: Pair<Int, Int>, val direction: Direction) {
        fun advance(): BeamDirection {
            return this.copy(coords = coords.first + direction.changeY to coords.second + direction.changeX)
        }
    }


    fun part1(input: List<String>): Int {
        val energized = mutableSetOf(0 to 0)
        val beamStarts = mutableSetOf(BeamDirection(0 to 0, Direction.RIGHT))
        val startedBeams = mutableSetOf<BeamDirection>()
        val grid = input.map { it.toList() }

        while (beamStarts.isNotEmpty()) {
            var currentBeam = beamStarts.first()
            beamStarts.remove(currentBeam)
            if (!startedBeams.add(currentBeam)) {
                "Not executing $currentBeam again".println()
                continue
            }
            "Starting $currentBeam - ${beamStarts.size} remaining".println()

            // Allow the beam to travel forever
            while (true) {
                val (y, x) = currentBeam.coords
                "Beam is now at $currentBeam".println()

                // Bounds check
                if (y !in grid.indices || x !in grid.first().indices) {
                    "Stopping $currentBeam out of bounds".println()
                    break
                }

                val currentSpace = grid[y][x]
                "Currently visiting $currentSpace".println()
                energized.add(currentBeam.coords)
                when (currentSpace) {
                    '.' -> currentBeam = currentBeam.advance()
                    '|' -> {
                        when (currentBeam.direction) {
                            Direction.UP, Direction.DOWN -> currentBeam = currentBeam.advance()
                            else -> {
                                beamStarts.add(currentBeam.copy(direction = Direction.UP).advance())
                                beamStarts.add(currentBeam.copy(direction = Direction.DOWN).advance())
                                break
                            }
                        }
                    }

                    '-' -> {
                        when (currentBeam.direction) {
                            Direction.LEFT, Direction.RIGHT -> currentBeam = currentBeam.advance()
                            else -> {
                                beamStarts.add(currentBeam.copy(direction = Direction.LEFT).advance())
                                beamStarts.add(currentBeam.copy(direction = Direction.RIGHT).advance())
                                break
                            }
                        }
                    }

                    '\\' -> {
                        currentBeam = when (currentBeam.direction) {
                            Direction.RIGHT -> currentBeam.copy(direction = Direction.DOWN).advance()
                            Direction.LEFT -> currentBeam.copy(direction = Direction.UP).advance()
                            Direction.UP -> currentBeam.copy(direction = Direction.LEFT).advance()
                            Direction.DOWN -> currentBeam.copy(direction = Direction.RIGHT).advance()
                        }
                    }

                    '/' -> {
                        currentBeam = when (currentBeam.direction) {
                            Direction.RIGHT -> currentBeam.copy(direction = Direction.UP).advance()
                            Direction.LEFT -> currentBeam.copy(direction = Direction.DOWN).advance()
                            Direction.UP -> currentBeam.copy(direction = Direction.RIGHT).advance()
                            Direction.DOWN -> currentBeam.copy(direction = Direction.LEFT).advance()
                        }
                    }
                    else -> throw IllegalArgumentException("Where are we $currentSpace")
                }
            }
        }

        grid.mapIndexed {y, row -> row.mapIndexed { x, c     -> if (energized.contains(y to x)) '#' else '.'  }}
            .joinToString("\n") {it.joinToString("")}
            .println()

        return energized.size.also { it.println() }
    }

    check(part1(readInput("2023/Day16_Test")) == 46)

    val input = readInput("2023/Day16")
    part1(input).println()

    fun part2(input: List<String>): Int {
        TODO()
    }

    check(part2(readInput("2023/Day16_Test")) == 145)
    part2(input).println()
}
