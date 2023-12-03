package `2023`

import println
import readInput

fun main() {

    fun findPartNumberCoordinates(matrix: List<List<Char>>, line: List<Char>, x: Int, y: Int): List<Pair<Int, Int>> {
        val coordinates = mutableListOf<Pair<Int, Int>>()
        for (currentY in y..<(line.size)) {
            if (matrix[x][currentY].isDigit()) {
                coordinates.add(Pair(x, currentY))
            } else {
                break
            }
        }
        return coordinates
    }

    fun hasAdjacentSymbol(matrix: List<List<Char>>, partNumberCoordinates: List<Pair<Int, Int>>): Boolean {
        return partNumberCoordinates
            .flatMap { coordinates ->
                listOf(
                    // Row Above
                    Pair(coordinates.first - 1, coordinates.second - 1),
                    Pair(coordinates.first - 1, coordinates.second),
                    Pair(coordinates.first - 1, coordinates.second + 1),
                    // Current Row
                    Pair(coordinates.first, coordinates.second - 1),
                    Pair(coordinates.first, coordinates.second + 1),
                    // Row Below
                    Pair(coordinates.first + 1, coordinates.second - 1),
                    Pair(coordinates.first + 1, coordinates.second),
                    Pair(coordinates.first + 1, coordinates.second + 1),
                )
            }
            .filter { coordinates ->
                coordinates.first > 0 && coordinates.first < matrix.size &&
                        coordinates.second > 0 && coordinates.second < matrix[0].size
            }
            .any { coordinates ->
                val character = matrix[coordinates.first][coordinates.second]
                !(character.isDigit() || character == '.')
            }

    }

    fun part1(input: List<String>): Int {
        var partsFound = 0
        val matrix = input.map { line -> line.toList() }

        for (x in 0..<(matrix.size)) {
            val line = matrix[x]
            var y = 0
            while (y < line.size) {
                if (line[y].isDigit()) {
                    val partNumberCoordinates = findPartNumberCoordinates(matrix, line, x, y)
                    val isPartNumber = hasAdjacentSymbol(matrix, partNumberCoordinates)
                    if (isPartNumber) {
                        val partNumber =
                            partNumberCoordinates.map { matrix[it.first][it.second] }.joinToString("").toInt()
                        partsFound += partNumber
                    }
                    y += partNumberCoordinates.size
                } else {
                    y++
                }
            }
        }

        return partsFound
    }

    check(
        part1(
            listOf(
                "467..114..",
                "...*......",
                "..35..633.",
                "......#...",
                "617*......",
                ".....+.58.",
                "..592.....",
                "......755.",
                "...\$.*....",
                ".664.598.."
            )
        ) == 4361
    )


    fun part2(input: List<String>): Int {
        TODO()
    }

//    check(
//        part2(
//            listOf(
//                "London to Dublin = 464",
//                "London to Belfast = 518",
//                "Dublin to Belfast = 141"
//            )
//        ) == 982
//    )

    val input = readInput("2023/Day03")
    part1(input).println()
    part2(input).println()
}
