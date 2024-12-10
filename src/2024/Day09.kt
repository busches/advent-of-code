import println
import readInput

sealed class Block(val size: Int)
class FreeSpace(size: Int) : Block(size) {
    override fun toString(): String {
        return ".".repeat(size)
    }
}

class FileSpace(size: Int, val blockId: Int) : Block(size) {
    override fun toString(): String {
        return "$blockId".repeat(size)
    }
}

fun main() {
    val start = System.currentTimeMillis()

    fun getBlocks(input: List<String>): List<Block> {
        return buildList {
            var isFileSpace = true
            var blockId = 0
            input.first().map { number ->
                number.digitToInt()
            }.forEach { int ->
                add(
                    if (isFileSpace) {
                        FileSpace(int, blockId)
                    } else {
                        FreeSpace(int)
                    }
                )
                if (isFileSpace) {
                    blockId++
                }
                isFileSpace = !isFileSpace
            }
        }
    }

    fun part1(input: List<String>): Long {
        val blocks = getBlocks(input)

        // Convert to flat list, too tired to think of a way to do this with blocks for part 2 and I already rewrote getBlocks
        val flattenBlocks = buildList {
            blocks.forEach { block ->
                repeat(block.size) {
                    add(if (block is FileSpace) block.blockId else -1)
                }
            }
        }

        val blocksToShift = flattenBlocks.toMutableList()
        val shiftedBlocks = mutableListOf<Int>()
        while (blocksToShift.isNotEmpty()) {
            val block = blocksToShift.removeFirst()
            if (block >= 0) {
                shiftedBlocks.add(block)
            } else {
                var shifted = false
                if (blocksToShift.isEmpty()) {
                    break
                }
                do {
                    val newBlock = blocksToShift.removeLast()
                    if (newBlock >= 0) {
                        shiftedBlocks.add(newBlock)
                        shifted = true
                    }
                } while (!shifted)
            }
        }

        return shiftedBlocks
            .mapIndexed { index, number -> (index * number).toLong() }.sum()

    }


    fun part2(input: List<String>): Long {
        val blocks = getBlocks(input).toMutableList()


        for (fileSpaceIndex in (blocks.size - 1 downTo 0)) {
            if (blocks[fileSpaceIndex] is FileSpace) {
                val fileSpace = blocks[fileSpaceIndex] as FileSpace
                // Find a free space block starting at the front with the size, but not searching past where it's currently at
                freeSpaceSearch@ for (freeSpaceIndex in 0..fileSpaceIndex) {
                    if (blocks[freeSpaceIndex] is FreeSpace && blocks[freeSpaceIndex].size >= fileSpace.size) {
                        val freeSpace = blocks[freeSpaceIndex] as FreeSpace
                        if (fileSpace.size == freeSpace.size) {
                            blocks[fileSpaceIndex] = freeSpace
                            blocks[freeSpaceIndex] = fileSpace
                        } else {
                            // We have to split the block up
                            blocks[freeSpaceIndex] = fileSpace
                            blocks.add(freeSpaceIndex + 1, FreeSpace(freeSpace.size - fileSpace.size))

                            // now add back the original space, has to be +1 as we just added one up there
                            blocks[fileSpaceIndex + 1] = FreeSpace(fileSpace.size)
                            // We don't have to worry about merging the free blocks back together, as we won't shift anything back to the right
                        }
                        break@freeSpaceSearch
                    }
                }
            }



        }

        val flattenBlocks = buildList {
            blocks.forEach { block ->
                repeat(block.size) {
                    add(if (block is FileSpace) block.blockId else 0)
                }
            }
        }

        return flattenBlocks
            .mapIndexed { index, number -> (index * number).toLong() }.sum()
    }

    val sampleInput = """
        2333133121414131402
    """.trimIndent().lines()
    check(part1(sampleInput) == 1928L)

    val input = readInput("2024/Day09")
    part1(input).println()

    check(part2(sampleInput) == 2858L)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
