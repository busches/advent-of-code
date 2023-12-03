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

    fun wrapInSearchCoordinates(
        coordinates: Pair<Int, Int>,
        matrix: List<List<Char>>
    ) =
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
            .filter { coordinates ->
                coordinates.first >= 0 && coordinates.first < matrix.size &&
                        coordinates.second >= 0 && coordinates.second < matrix[0].size
            }

    fun hasAdjacentSymbol(matrix: List<List<Char>>, partNumberCoordinates: List<Pair<Int, Int>>): Boolean {
        return partNumberCoordinates
            .flatMap { wrapInSearchCoordinates(it, matrix) }
            .any { coordinates ->
                val character = matrix[coordinates.first][coordinates.second]
                !(character.isDigit() || character == '.')
            }

    }

    fun getPartListCoordinates(matrix: List<List<Char>>): List<List<Pair<Int, Int>>> {
        val partList = mutableListOf<List<Pair<Int, Int>>>()

        for (x in 0..<(matrix.size)) {
            val line = matrix[x]
            var y = 0
            while (y < line.size) {
                if (line[y].isDigit()) {
                    val partNumberCoordinates = findPartNumberCoordinates(matrix, line, x, y)
                    val isPartNumber = hasAdjacentSymbol(matrix, partNumberCoordinates)

                    if (isPartNumber) {
                        partList.add(partNumberCoordinates)
                    }
                    y += partNumberCoordinates.size
                } else {
                    y++
                }
            }
        }
        return partList
    }

    fun getPartNumbersOfGear(matrix: List<List<Char>>, partList: List<List<Pair<Int, Int>>>): List<List<Int>> {
        val gearList = mutableListOf<List<Int>>()

        for (x in 0..<(matrix.size)) {
            val line = matrix[x]
            var y = 0
            while (y < line.size) {
                if (line[y] == '*') {
                    val gearCoordinates = Pair(x, y)
                    val searchCoordinates = wrapInSearchCoordinates(gearCoordinates, matrix)
                    val partsNextToGear = partList.filter { partCoordinates ->
                        searchCoordinates.any { partCoordinates.contains(it) }
                    }.map { partCoordinates ->
                        partCoordinates.map { matrix[it.first][it.second] }.joinToString("").toInt()
                    }
                    if (partsNextToGear.size > 1) {
                        gearList.add(partsNextToGear)
                    }
                }
                y++
            }
        }
        return gearList
    }


    fun part1(input: List<String>): Int {
        val matrix = input.map { line -> line.toList() }
        return getPartListCoordinates(matrix)
            .sumOf { partList -> partList.map { matrix[it.first][it.second] }.joinToString("").toInt() }
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

    val input = readInput("2023/Day03")
    part1(input).println()


    fun part2(input: List<String>): Int {
        val matrix = input.map { line -> line.toList() }
        val partList = getPartListCoordinates(matrix)
        return getPartNumbersOfGear(matrix, partList)
            .sumOf { it.first() * it.last() }
    }

    check(
        part2(
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
        ) == 467835
    )

    part2(input).println()
}
