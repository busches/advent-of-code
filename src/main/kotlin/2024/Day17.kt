package `2024`

import utils.println
import utils.readInput
import java.text.DecimalFormat
import java.time.LocalDateTime
import kotlin.math.pow

data class Registers(
    val a: Long,
    val b: Int,
    val c: Int,
    val output: List<Int> = emptyList(),
    val nextInstruction: Int = -1
) {
    fun updateRegister(opcode: Int, operand: Int): Registers {
        val comboOperand = when (operand) {
            4 -> a
            5 -> b.toLong()
            6 -> c.toLong()
            else -> operand.toLong()
        }
        return when (opcode) {
            0 -> copy(a = a.div(2.toDouble().pow(comboOperand.toDouble())).toLong())
            1 -> copy(b = b.xor(operand))
            2 -> copy(b = comboOperand.mod(8))
            3 -> if (a == 0L) this else copy(nextInstruction = operand)
            4 -> copy(b = b.xor(c))
            5 -> copy(output = output + comboOperand.mod(8))
//            6 -> copy(b = a.div(2.toDouble().pow(comboOperand.toDouble())).toLong())
            7 -> copy(c = a.div(2.toDouble().pow(comboOperand.toDouble())).toInt())
            else -> throw IllegalArgumentException("Opcode $opcode not supported")
        }
    }
}


fun runProgram(register: Registers, allInstructions: List<Int>, part2: Boolean = false): Registers {
    var updatedRegister = register

    var currentInstruction = 0
    while (currentInstruction < allInstructions.size) {
        updatedRegister = updatedRegister.updateRegister(
            allInstructions[currentInstruction], allInstructions[currentInstruction + 1]
        )

        if (part2 && updatedRegister.output.isNotEmpty()) {
            if (updatedRegister.output != allInstructions.take(updatedRegister.output.size)) {
                return Registers(0, 0, 0)
            }
        }

        if (updatedRegister.nextInstruction >= 0) {
            currentInstruction = updatedRegister.nextInstruction
            updatedRegister = updatedRegister.copy(nextInstruction = -1)
        } else {
            currentInstruction += 2
        }
    }

    return updatedRegister
}

fun main() {
    val start = System.currentTimeMillis()

    fun mapProgramInstructionsAndRegister(input: List<String>): Pair<List<Int>, Registers> {
        var a = 0L
        var b = 0
        var c = 0
        var programInstructions = listOf<Int>()
        input.forEach { line ->
            when {
                line.contains("Register A") -> a = line.substringAfter(":").trim().toLong()
                line.contains("Register B") -> b = line.substringAfter(":").trim().toInt()
                line.contains("Register C") -> c = line.substringAfter(":").trim().toInt()
                line.contains("Program") -> {
                    programInstructions = line.substringAfter(":").split(",").map { it.trim().toInt() }
                }
            }
        }

        val registers = Registers(a, b, c)
        return Pair(programInstructions, registers)
    }

    fun part1(input: List<String>): String {
        val (programInstructions, registers) = mapProgramInstructionsAndRegister(input)

        return runProgram(registers, programInstructions).output.joinToString(",")
    }

    fun part2(input: List<String>): Long {
        val (programInstructions, registers) = mapProgramInstructionsAndRegister(input)
        var a = 0L
        val dec = DecimalFormat("#,###.##")
        while (true) {
            val output = runProgram(registers.copy(a = a), programInstructions, true).output
            if (output == programInstructions) {
                return a.also { "Found the answer!".println() }
            } else {
                if (a % 10_000_000 == 0L) "${LocalDateTime.now()} - A is ${dec.format(a)}.".println()
                a++
            }
        }
    }

    val sampleInput = """
        Register A: 729
        Register B: 0
        Register C: 0
        
        Program: 0,1,5,4,3,0
    """.trimIndent().lines()
    check(part1(sampleInput) == "4,6,3,5,6,3,5,2,1,0")

    val input = readInput("2024/Day17")
    part1(input).println()

    val sampleInputPart2 = """
        Register A: 2024
        Register B: 0
        Register C: 0

        Program: 0,3,5,4,3,0
    """.trimIndent().lines()
    check(part2(sampleInputPart2) == 117440L)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}
