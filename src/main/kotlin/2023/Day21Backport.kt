package day21


import java.lang.Math.floorMod
import kotlin.io.path.Path
import kotlin.io.path.readText

fun main(){
    Day21.part2().let(::println)
}

object Day21 : Challenge() {
    val parsed = input.lines().flatMapIndexed { y, c -> c.mapIndexed { x, c -> y to x to c } }.toMap()
    val startPoint = parsed.entries.first { it.value == 'S' }.key
    private val height = parsed.maxOf { it.key.first } + 1
    private val width = parsed.maxOf{ it.key.second } + 1

    fun Point.pq() = floorMod(first, height) to floorMod(second, width)

    data class State(
        val previousScore: Long = 0L,
        val previousFrontier: Set<Point> = emptySet(),
        val currentFrontier: Set<Point> = setOf(startPoint),
        val score: Long = 1L,
    ) {
        val memoryState by lazy { currentFrontier.map { it.pq() }.toSet() }
    }

    private val states = sequence{
        var state = State()
        while(true){
            yield(state)
            val newFrontier = mutableSetOf<Point>()
            for(point in state.currentFrontier){
                for(neighbour in point.cardinals()){
                    val puPoint = neighbour.pq()
                    if(parsed[puPoint] != '#' && neighbour !in state.previousFrontier){
                        newFrontier.add(neighbour)
                    }
                }
            }
            state = State(state.score, state.currentFrontier, newFrontier, state.previousScore + newFrontier.size)
        }
    }

    fun solveGeneric(depth: Int): Long {
        val memory = mutableMapOf<Set<Point>, MutableList<Long>>().withDefault { mutableListOf() }
        for(state in states){
            when(memory[state.memoryState]?.size){
                3 -> break
                else -> memory[state.memoryState] = memory.getValue(state.memoryState).apply { add(state.score) }
            }
        }

        val answers = memory.values.toList()
        val indexOfCycles = answers.indexOfFirst { it.size >= 3 }
        val justValues = answers.subList(0, indexOfCycles).map { it.first() }

        return when(depth){
            in 0..<indexOfCycles -> justValues[depth]
            else -> {
                val formulas = answers.subList(indexOfCycles, answers.size).map { (y1, y2, y3) -> toQuadratic(y1, y2, y3) }
                val sizeOfCycle = formulas.size
                val iteration = (depth - indexOfCycles) / sizeOfCycle + 1
                val indexInsideCycle = (depth - indexOfCycles) % sizeOfCycle
                formulas[indexInsideCycle].let { (a, b, c) -> a * iteration * iteration + b * iteration + c }
            }
        }
    }

    fun toQuadratic(y1: Long, y2: Long, y3: Long): Triple<Long, Long, Long>{
        val a = ((y3 - y2) - (y2 - y1)) / 2
        val b = (y2 - y1) - 3 * a
        val c = y1 - (a + b)
        return Triple(a, b, c)
    }

    override fun part2() = solveGeneric(26501365)

}

abstract class Challenge {
    val input: String = Path("src/2023/Day21.txt").readText()
    abstract fun part2(): Any?


}

typealias Point = Pair<Int, Int>

val NORTH = -1 to 0
val EAST = 0 to 1
val SOUTH = 1 to 0
val WEST = 0 to -1
operator fun Point.plus(other: Point) = first + other.first to second + other.second
operator fun Point.minus(other: Point) = first - other.first to second - other.second

operator fun Point.times(amount: Int) = first * amount to second * amount

val CARDINALS = listOf(NORTH, EAST, SOUTH, WEST)
fun Point.cardinals() = CARDINALS.map { it + this }

operator fun Point.unaryMinus() = -first to -second
operator fun Point.dec() = rotLeft()
operator fun Point.inc() = rotRight()
fun Point.rotRight() = -second to first
fun Point.rotLeft() = second to -first

operator fun Point.not() = sequenceOf(rotLeft(), -this, rotRight())

