package `2023`

import utils.println
import utils.readInput

fun main() {

    fun hashAlgo(input: String): Int {
        return input
            .map { it.code }
            .fold(0) { acc, letter ->
                (acc + letter) * 17 % 256
            }
            // .also { "$input -> $it".println() }
    }

    check(hashAlgo("HASH") == 52)

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf { hashAlgo(it) }
    }

    check(part1(readInput("2023/Day15_Test")) == 1320)

    val input = readInput("2023/Day15")
    part1(input).println()

    fun part2(input: List<String>): Int {
        val boxes = MutableList(256) { mutableListOf<Pair<String, Int>>() }

        input.first().split(",").forEach { rawLens ->
            when {
                rawLens.contains("=") -> {
                    val (label, focalLength) = rawLens.split("=")
                    val hash = hashAlgo(label)
                    val box = boxes[hash]
                    val hasExistingLens = box.any { it.first == label }
                    if (hasExistingLens) {
                        boxes[hash] = box.map {
                            if (it.first == label) {
                                label to focalLength.toInt()
                            } else {
                                it
                            }
                        }.toMutableList()
                    } else {
                        box.add(label to focalLength.toInt())
                    }
                }

                rawLens.contains("-") -> {
                    val (label) = rawLens.split("-")
                    val hash = hashAlgo(label)
                    val box = boxes[hash]
                    boxes[hash] = box.filter { it.first != label }.toMutableList()
                }

                else -> throw IllegalArgumentException("What is this $rawLens")
            }
        }

        return boxes.flatMapIndexed { boxIndex, box ->
            box.mapIndexed { lensIndex, lens ->
                (boxIndex + 1) * (lensIndex + 1) * lens.second
            }
        }.sum()
    }

    check(part2(readInput("2023/Day15_Test")) == 145)


    part2(input).println()
}
