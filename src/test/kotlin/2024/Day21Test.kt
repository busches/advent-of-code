package `2024`

import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Test {

    @Test
    fun `return all possible ways to enter 029A`() {
        assertEquals(listOf("<A^A>^^AvvvA", "<A^A^>^AvvvA", "<A^A^^>AvvvA"), Day21.getRobotOneKeyPresses("029A"))
    }
}
