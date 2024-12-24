package `2024`

import kotlin.test.Test
import kotlin.test.assertEquals


class Day22Test {

    @Test
    fun `get next secrets of 123`() {
        val startingSecret = 123L

        var newSecret = Day22.generateNextSecret(startingSecret)
        assertEquals(15887950L, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(16495136, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(527345, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(704524, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(1553684, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(12683156, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(11100544, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(12249484, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(7753432, newSecret)
        newSecret = Day22.generateNextSecret(newSecret)
        assertEquals(5908254, newSecret)
    }

    @Test
    fun `generates next day secrets`() {
        assertEquals(8685429, Day22.generateEntireDaysSecret(1))
        assertEquals(4700978, Day22.generateEntireDaysSecret(10))
        assertEquals(15273692, Day22.generateEntireDaysSecret(100))
        assertEquals(8667524, Day22.generateEntireDaysSecret(2024))
    }

    @Test
    fun `Part1 sums up the 200th secret for each line`() {
        val input = """
            1
            10
            100
            2024
        """.trimIndent().lines()

        assertEquals(37327623, Day22.part1(input))
    }

    @Test
    fun `Get secret prices from secret number`() {
        val startingSecret = 123L

        val prices = Day22.getAllSecretPricesFromSecret(startingSecret)

        val expectedPrices = listOf(3, 0, 6, 5, 4, 4, 6, 4, 4, 2)

        assertEquals(expectedPrices, prices.take(10))
    }

    @Test
    fun `Get Indexed Price Sequences from Secret Price List`() {
        val priceList = listOf(3, 0, 6, 5, 4, 4, 6, 4, 4, 2)

        val expectedPriceSequence = mapOf(
            listOf(-3, 6, -1, -1) to 4,
            listOf(6, -1, -1, 0) to 4,
            listOf(-1, -1, 0, 2) to 6,
            listOf(-1, 0, 2, -2) to 4,
            listOf(0, 2, -2, 0) to 4,
            listOf(2, -2, 0, -2) to 2,
        )

        assertEquals(expectedPriceSequence, Day22.getIndexedPriceSequences(priceList))
    }

    @Test
    fun `Get Indexed Price Sequences from Secret Price List doesn't replace existing entries`() {
        val priceList = listOf(6, 5, 4, 3, 2, 1)

        val expectedPriceSequence = mapOf(
            listOf(-1, -1, -1, -1) to 2
        )

        assertEquals(expectedPriceSequence, Day22.getIndexedPriceSequences(priceList))
    }

    @Test
    fun `Part 2 get sum of prices for sequence`() {
        val input = """
            1
            2
            3
            2024
        """.trimIndent().lines()

        assertEquals(23, Day22.part2(input))
    }
}
