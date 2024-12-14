package `2024`

import println
import readInput

private typealias Coordinates = Pair<Int, Int>

private data class Move(val coords: Coordinates, val tokens: Int, val aPresses: Int, val bPresses: Int)
sealed class Button(val move: Coordinates, val cost: Int) {}
class AButton(xMovement: Int, yMovement: Int) : Button(xMovement to yMovement, 3) {
    override fun toString(): String {
        return "AButton(x='${move.first}', y='${move.second}', cost=$cost)"
    }
}

class BButton(xMovement: Int, yMovement: Int) : Button(xMovement to yMovement, 1) {
    override fun toString(): String {
        return "BButton(x='${move.first}', y='${move.second}', cost=$cost)"
    }
}

private data class Game(val aButton: Button, val bButton: Button, val prize: Coordinates)

fun main() {
    val start = System.currentTimeMillis()

    fun part1(input: List<String>): Int {
        val buttonRegex = """.*X\+(\d+), Y\+(\d+)""".toRegex()
        val prizeRegex = """.*X=(\d+), Y=(\d+)""".toRegex()
        val games = input.chunked(4).map { (buttonA, buttonB, prize, _) ->
            val (aX, aY) = buttonRegex.matchEntire(buttonA)!!.destructured
            val (bX, bY) = buttonRegex.matchEntire(buttonB)!!.destructured
            val (prizeX, prizeY) = prizeRegex.matchEntire(prize)!!.destructured

            Game(
                AButton(aX.toInt(), aY.toInt()),
                BButton(bX.toInt(), bY.toInt()),
                Coordinates(prizeX.toInt(), prizeY.toInt())
            )
        }

        return games.fold(0) { total, game ->
            val startingMove = Move(0 to 0, 0, 0, 0)
            val moves = ArrayDeque(listOf(startingMove))
            val finishedMoves = mutableListOf<Move>()
            val cachedMoves = mutableSetOf<Move>()
            while (moves.isNotEmpty()) {
                val move = moves.removeFirst()
                if (!cachedMoves.add(move)) {
                    continue
                }
                if (move.aPresses > 100 || move.bPresses > 100) {
                    continue
                }
                else if (move.coords.first > game.prize.first || move.coords.second > game.prize.second) {
                    continue
                }
                else if (move.coords == game.prize) {
                    finishedMoves.add(move)
                }
                else {
                    moves.addFirst(
                        Move(
                            move.coords + game.aButton.move,
                            move.tokens + game.aButton.cost,
                            move.aPresses + 1,
                            move.bPresses
                        )
                    )
                    moves.addFirst(
                        Move(
                            move.coords + game.bButton.move,
                            move.tokens + game.bButton.cost,
                            move.aPresses,
                            move.bPresses + 1
                        )
                    )
                }
            }

            total + (finishedMoves.minOfOrNull { it.tokens } ?: 0)
        }.also { it.println() }
    }


    fun part2(input: List<String>): Int {
        TODO()
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
    check(part1(sampleInput) == 480)

    val input = readInput("2024/Day13")
    part1(input).println()

    check(part2(sampleInput) == 982)
    part2(input).println()

    "${(System.currentTimeMillis() - start)} milliseconds".println()
}

private operator fun Pair<Int, Int>.plus(currentPosition: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + currentPosition.first to this.second + currentPosition.second
}
