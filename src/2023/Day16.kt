package `2023`

import println
import readInput

fun main() {

    data class BeamDirection(val coords: Pair<Int, Int>, val direction: Direction) {
        fun advance(): BeamDirection {
            return this.copy(coords = coords.first + direction.changeY to coords.second + direction.changeX)
        }
    }

    fun panelsEnergized(startingBeam: BeamDirection, grid: List<List<Char>>): Int {
        val energized = mutableSetOf<Pair<Int, Int>>()
        val beamStarts = mutableSetOf(startingBeam)
        val startedBeams = mutableSetOf<BeamDirection>()

        while (beamStarts.isNotEmpty()) {
            var currentBeam = beamStarts.first()
            beamStarts.remove(currentBeam)
            if (!startedBeams.add(currentBeam)) {
                continue
            }

            // Allow the beam to travel forever
            while (true) {
                val (y, x) = currentBeam.coords

                // Bounds check
                if (y !in grid.indices || x !in grid.first().indices) {
                    break
                }

                val currentSpace = grid[y][x]
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

        "Starting - $startingBeam".println()
        grid.mapIndexed { y, row -> row.mapIndexed { x, c -> if (energized.contains(y to x)) '#' else '.' } }
            .joinToString("\n") { it.joinToString("") }
            .println()

        return energized.size
    }

    fun part1(input: List<String>): Int {
        val startingBeam = BeamDirection(0 to 0, Direction.RIGHT)
        return panelsEnergized(startingBeam, input.map { it.toList() })
    }

    check(part1(readInput("2023/Day16_Test")) == 46)

    val input = readInput("2023/Day16")
    part1(input).println()

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toList() }
        val beamsFromTop = grid.first().indices.map { BeamDirection(0 to it, Direction.DOWN) }
        val beamsFromBottom = grid.first().indices.map { BeamDirection(grid.first().size - 1 to it, Direction.UP) }
        val beamsFromLeft = grid.indices.map { BeamDirection(it to 0, Direction.RIGHT )}
        val beamsFromRight = grid.indices.map { BeamDirection(it to grid.size - 1, Direction.LEFT )}

        val allStartingBeams = beamsFromTop + beamsFromLeft + beamsFromRight + beamsFromBottom

        return allStartingBeams.maxOf { panelsEnergized(it, grid) }
    }

    check(part2(readInput("2023/Day16_Test")) == 51)
    part2(input).println()
}
