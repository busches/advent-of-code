package `2015`

import println
import readInput

fun main() {

    val extract = "(.*) can fly (.*) km/s for (.*) seconds, but then must rest for (.*) seconds.".toRegex()

    data class Reindeer(val name: String, val speed: Int, val flightTime: Int, val restTime: Int) {
        fun distanceFlown(totalTime: Int): Int {
            var travelDistance = 0
            var timeElapsed = 0
            var restTimer = 0
            var travelTimer = 0

            var isFlying = true

            while (timeElapsed != totalTime) {
                timeElapsed++
                if (isFlying) {
                    if (travelTimer < flightTime) {
                        travelDistance += speed
                        travelTimer++
                    } else {
                        isFlying = false
                        travelTimer = 0
                        restTimer++
                    }
                } else {
                    if (restTimer < restTime) {
                        restTimer++
                    } else {
                        isFlying = true
                        restTimer = 0
                        travelDistance += speed
                        travelTimer++
                    }
                }
            }
            return travelDistance
        }
    }

    check(Reindeer("Comet", 14, 10, 127).distanceFlown(1000) == 1120)
    check(Reindeer("Dancer", 16, 11, 162).distanceFlown(1000) == 1056)

    fun extractValues(input: String): Reindeer {
        val (name, speed, flightTime, restTime) = extract.find(input)!!.destructured

        return Reindeer(name, speed.toInt(), flightTime.toInt(), restTime.toInt())
    }

    fun part1(input: List<String>, time: Int): Int {
        return input.map(::extractValues)
            .maxOf { it.distanceFlown(time) }
    }

    check(
        part1(
            listOf(
                "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.",
            ), 1000
        ) == 1120
    )

    val input = readInput("2015/Day14")
    part1(input, 2503).println()


    fun part2(input: List<String>, time: Int): Int {
        TODO()
    }

    check(
        part2(
            listOf(
                "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.",
            ), 1000
        ) == 689
    )

    part2(input, 2503).println()
}
