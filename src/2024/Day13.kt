package `2024`

import println
import readInput

private typealias Coordinates = Pair<Long, Long>

sealed class Button(val move: Coordinates, val cost: Int)
class AButton(xMovement: Long, yMovement: Long) : Button(xMovement to yMovement, 3) {
    override fun toString(): String {
        return "AButton(x='${move.first}', y='${move.second}', cost=$cost)"
    }
}

class BButton(xMovement: Long, yMovement: Long) : Button(xMovement to yMovement, 1) {
    override fun toString(): String {
        return "BButton(x='${move.first}', y='${move.second}', cost=$cost)"
    }
}

private data class Game(val aButton: Button, val bButton: Button, val prize: Coordinates)

fun main() {
    val start = System.currentTimeMillis()

    fun solve(input: List<String>, part2: Boolean): Long {
        val buttonRegex = """.*X\+(\d+), Y\+(\d+)""".toRegex()
        val prizeRegex = """.*X=(\d+), Y=(\d+)""".toRegex()
        val games = input.chunked(4).map { (buttonA, buttonB, prize, _) ->
            val (aX, aY) = buttonRegex.matchEntire(buttonA)!!.destructured
            val (bX, bY) = buttonRegex.matchEntire(buttonB)!!.destructured
            val (prizeX, prizeY) = prizeRegex.matchEntire(prize)!!.destructured

            var prizeCoordinates = Coordinates(prizeX.toLong(), prizeY.toLong())
            if (part2) {
                prizeCoordinates += 10000000000000L to 10000000000000L
            }
            Game(
                AButton(aX.toLong(), aY.toLong()),
                BButton(bX.toLong(), bY.toLong()),
                prizeCoordinates
            )
        }


        return games.fold(0L) { total, game ->
            // Solve the equation - https://getyarn.io/yarn-clip/56eab036-62be-41c7-8236-0a033cf74b28
            // aPresses * aButtonX + bPresses * bButtonX = prizeX
            // -> aPresses * aButtonX = prizeX - (bPresses * bButtonX)
            // -> aPresses = (prizeX - (bPresses * bButtonX))/aButtonX

            // aPresses * aButtonY + bPresses * bButtonY = prizeY
            // -->  bPresses * bButtonY = prizeY - (aPresses * aButtonY)
            // -->  bPresses  = (prizeY - aPresses * aButtonY)/bButtonY

            // Now solve for aPresses!
            // aPresses = (prizeX - (((prizeY - aPresses * aButtonY)/bButtonY) * bButtonX)) /a ButtonX
            // --> aPresses * aButtonX = prizeX - ((prizeY - aPresses * aButtonY) / bButtonY) * bButtonX
            // --> aPresses * aButtonX = prizeX - ((prizeY - aPresses * aButtonY) * bButtonX)/bButtonY
            // --> aPresses * aButtonX * bButtonY = prizeX * bButtonY - ((prizeY - aPresses * aButtonY) * bButtonX)
            // --> aPresses * aButtonX * bButtonY + aPresses * aButtonY * bButtonX = prizeX * bButtonY − prizeY * bButtonX
            // --> aPresses * (aButtonX * bButtonY + aButtonY * bButtonX) = prizeX * bButtonY − prizeY * bButtonX
            // --> aPresses  = (prizeX * bButtonY − prizeY * bButtonX) /(aButtonX * bButtonY + aButtonY * bButtonX)

            // Same for bPresses
            val (prizeX, prizeY) = game.prize
            val (bButtonX, bButtonY) = game.bButton.move
            val (aButtonX, aButtonY) = game.aButton.move
            // Force one to be a double, otherwise we have to recheck the numbers to see if they actually work
            val aPresses =
                (prizeX * bButtonY - prizeY * bButtonX) / (aButtonX.toDouble() * bButtonY - aButtonY * bButtonX)
            val bPresses =
                (prizeY * aButtonX - prizeX * aButtonY) / (aButtonX.toDouble() * bButtonY - aButtonY * bButtonX)

            if (aPresses.toLong().compareTo(aPresses) == 0 && bPresses.toLong().compareTo(bPresses) == 0) {
                aPresses.toLong() * 3 + bPresses.toLong() + total
            } else {
                total
            }
        }.also { it.println() }
    }

    fun part1(input: List<String>): Long {
        return solve(input, false)
    }

    fun part2(input: List<String>): Long {
        return solve(input, true)
    }


    val sampleInput = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400
        
        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176
        
        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450
        
        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent().lines()
    check(part1(sampleInput) == 480L)

    val input = readInput("2024/Day13")
    part1(input).println()

    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}

private operator fun Pair<Long, Long>.plus(currentPosition: Pair<Long, Long>): Pair<Long, Long> {
    return this.first + currentPosition.first to this.second + currentPosition.second
}
