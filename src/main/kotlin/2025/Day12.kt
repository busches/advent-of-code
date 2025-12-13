package `2025`

import utils.println
import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {

    fun part1(input: String): Long {
        val presents = input
            .split("\n\n")
            .dropLast(1)
            .associate { section ->
                val lines = section.lines()
                val index = lines.first().take(1)
                val present = lines.drop(1)
                index to present
            }

        val regions = input
            .split("\n\n")
            .takeLast(1)
            .flatMap { everything ->
                val sections = everything.split("\n")
                sections.map { section ->
                    val (size, presents) = section.split(":")
                    val presentSizes = presents.trim().split(" ").map { it.toInt() }
                    val (x, y) = size.split("x").map { it.toInt() }
                    x to y to presentSizes
                }
            }


        return regions.sumOf { region ->
            val (coords, presentSizes) = region
            val (x, y) = coords
            val sizeOfRegion = x * y
            val sizeOfPresents = presentSizes.mapIndexed { index, presentCount ->
                if (presentCount > 0) {
                    val present = presents[index.toString()]!!
                    val presentSize = present.sumOf { it.toList().count { char -> char == '#' } }
                    presentSize * presentCount
                } else 0
            }.sum()
            "$region has a size of $sizeOfRegion and presents are $sizeOfPresents".println()
           
            TODO("No idea how to move these around that won't take 20 years :)")
        }
    }

    val sampleInput = """
        0:
        ###
        ##.
        ##.
        
        1:
        ###
        ##.
        .##
        
        2:
        .##
        ###
        ##.
        
        3:
        ##.
        ###
        ##.
        
        4:
        ###
        #..
        ###
        
        5:
        ###
        .#.
        ###
        
        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
        12x5: 1 0 1 0 3 2
    """.trimIndent()
    check(part1(sampleInput) == 2L)

    fun part2(input: String): Long {
        TODO()
    }

    val input = Path("src/main/kotlin/${"2025/Day11"}.txt").readText()
    part1(input).println()


    check(part2(sampleInput) == 2L)

    part2(input).println()
}


