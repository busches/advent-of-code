package `2015`

import kotlinx.serialization.json.*
import println
import readInput
import java.lang.RuntimeException

fun extractFromObject(json: JsonObject): Int {
    return if (json.containsValue(JsonPrimitive("red"))) {
        0
    } else {
        json.values.sumOf {
            when {
                it is JsonPrimitive -> it.toString().toIntOrNull() ?: 0
                it is JsonArray -> extractFromArray(it)
                it is JsonObject -> extractFromObject(it)
                else -> throw RuntimeException("Unknown type ${it.javaClass}")
            }
        }
    }
}

fun extractFromArray(json: JsonArray): Int {
    return json.sumOf {
        when {
            it is JsonPrimitive -> it.toString().toIntOrNull() ?: 0
            it is JsonArray -> extractFromArray(it)
            it is JsonObject -> extractFromObject(it)
            else -> throw RuntimeException("Unknown type ${it.javaClass}")
        }
    }
}

fun main() {

    val numbersRegex = "(-*\\d+)".toRegex()
    fun part1(input: List<String>): Int {
        return numbersRegex.findAll(input.first())
            .map { it.value }
            .map { it.toInt() }
            .sum()
    }

    check(
        part1(
            listOf(
                "{\"a\":{\"b\":4},\"c\":-1}"
            )
        ) == 3
    )





    fun part2(input: List<String>): Int {
        // Guess we have to actually parse the json now


        val json = Json.decodeFromString<JsonElement>(input.first())
        return if (json is JsonArray) {
            extractFromArray(json)
        } else if (json is JsonObject) {
            extractFromObject(json)
        } else {
            TODO()
        }
    }

    check(part2(listOf("[1,{\"c\":\"red\",\"b\":2},3]")) == 4)
    check(part2(listOf("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}")) == 0)
    check(part2(listOf("[1,\"red\",5]")) == 6)

    val input = readInput("2015/Day12")
    part1(input).println()
    part2(input).println()
}
