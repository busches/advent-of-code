package `2024`

import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Test {

    @Test
    fun `If register C contains 9, the program 2,6 would set register B to 1`() {
        val register = Registers(1, 2, 9)
        val instructions = listOf(2, 6)

        val updatedRegister = register.updateRegister(instructions)
        assertEquals(1, updatedRegister.b)
    }

    @Test
    fun `If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2`() {
        val register = Registers(10, 2, 9)

        assertEquals(listOf(0, 1, 2), runProgram(register, listOf(5, 0, 5, 1, 5, 4)).output)
    }

    @Test
    fun `If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A`() {
        val register = Registers(2024, 0, 0)
        val allInstructions = listOf(0, 1, 5, 4, 3, 0)

        val expectedRegister = Registers(0, 0, 0, output = listOf(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0))
        assertEquals(expectedRegister, runProgram(register, allInstructions))
    }

    @Test
    fun `If register B contains 29, the program 1,7 would set register B to 26`() {
        val register = Registers(0, 29, 0)
        val allInstructions = listOf(1, 7)

        assertEquals(26, runProgram(register, allInstructions).b)
    }

    @Test
    fun `If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354`() {
        val register = Registers(0, 2024, 43690)
        val allInstructions = listOf(4, 0)

        assertEquals(44354, runProgram(register, allInstructions).b)
    }
}
