package `2023`

import println
import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    check(Day19().part1(readInput("2023/Day19_Test")) == 19114L).also { "Check Part1 passed".println() }
    Day19().part1(readInput("2023/Day19")).println()
    check(Day19().part2(readInput("2023/Day19_Test")) == 167_409_079_868_000L).also { "Check Part2 passed".println() }
    Day19().part2(readInput("2023/Day19")).println()
}

class Day19 {
    fun part1(input: List<String>): Long {
        val (workflows, parts) = parseInput(input)

        return parts
            .filter { part -> processPart(workflows, "in", part) }
            .sumOf { (x, m, a, s) -> x.toLong() + m + a + s }
    }

    fun part2(input: List<String>): Long {
        val (workflows, _) = parseInput(input)

        // Start with these mofos
        val allTheRanges = mapOf(
            "x" to 1..4000,
            "m" to 1..4000,
            "a" to 1..4000,
            "s" to 1..4000,
        )

        val workflowsToProcess = ArrayDeque<Pair<String, Map<String, IntRange>>>()
        workflowsToProcess += "in" to allTheRanges

        var combos = 0L
        while (workflowsToProcess.isNotEmpty()) {

            val (workflowKey, ranges) = workflowsToProcess.removeFirst()

            val currentWorkflow = workflows[workflowKey]!!
            val defaultRanges = ranges.toMutableMap()
            currentWorkflow.rules.forEach { rule ->
                val conditionMatchRanges = defaultRanges.toMutableMap()
                val rangeForRule = defaultRanges[rule.field]!!
                if (rule.expression == ">") {
                    conditionMatchRanges[rule.field] = max(rangeForRule.first, rule.value + 1)..rangeForRule.last
                    defaultRanges[rule.field] = rangeForRule.first..min(rangeForRule.last, rule.value)
                } else {
                    conditionMatchRanges[rule.field] = rangeForRule.first..min(rangeForRule.last, rule.value - 1)
                    defaultRanges[rule.field] = max(rangeForRule.first, rule.value)..rangeForRule.last
                }

                when (rule.nextWorkflow) {
                    "A" -> {
                        val newCombos =
                            conditionMatchRanges.values.fold(1L) { acc, range -> acc * (range.last - range.first + 1) }
                        "Adding conditional ranges: $conditionMatchRanges for $newCombos to $combos".println()
                        combos += newCombos
                    }

                    "R" -> {} // Do nothing
                    else -> workflowsToProcess.add(rule.nextWorkflow to conditionMatchRanges)
                }
            }
            when (currentWorkflow.default) {
                "A" -> {
                    val newCombos = defaultRanges.values.fold(1L) { acc, range -> acc * (range.last - range.first + 1) }
                    "Adding default ranges: $defaultRanges for $newCombos to $combos".println()
                    combos += newCombos
                }

                "R" -> {} // Do nothing
                else -> workflowsToProcess.add(currentWorkflow.default to defaultRanges)
            }
        }

        return combos.also { it.println() }
    }

    private fun processPart(workflows: Map<String, Workflow>, workflowKey: String, part: Part): Boolean {
        val currentWorkflow = workflows[workflowKey]!!
        val nextWorkflow = currentWorkflow.rules.firstOrNull { rule ->
            val fieldValue = when (rule.field) {
                "x" -> part.x
                "m" -> part.m
                "a" -> part.a
                "s" -> part.s
                else -> throw IllegalArgumentException("What field is this? ${rule.field}")
            }
            if (rule.expression == ">") {
                fieldValue > rule.value
            } else {
                fieldValue < rule.value
            }
        }?.nextWorkflow ?: currentWorkflow.default

        return when (nextWorkflow) {
            "A" -> true
            "R" -> false
            else -> processPart(workflows, nextWorkflow, part)
        }
    }

    private val workflowRegex = "(.*)\\{(.*)}".toRegex()
    private val ruleRegex = "(.*)([<>])(.*):(.*)".toRegex()
    private val partRegex = "\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}".toRegex()
    fun parseInput(input: List<String>): Pair<Map<String, Workflow>, List<Part>> {
        var parseWorkflows = true
        val workflows = mutableMapOf<String, Workflow>()
        val parts = mutableListOf<Part>()

        input.forEach { line ->
            if (line.isBlank()) {
                parseWorkflows = false
                return@forEach
            }
            if (parseWorkflows) {
                val (key, rawRules) = workflowRegex.find(line)!!.destructured
                val allRules = rawRules.split(",")
                val default = allRules.last()
                val rules = allRules.dropLast(1).map { rule ->
                    val (field, expression, value, nextWorkflow) = ruleRegex.find(rule)!!.destructured
                    Rule(field, expression, value.toInt(), nextWorkflow)
                }
                workflows[key] = Workflow(key, rules, default)
            } else {
                val (x, m, a, s) = partRegex.find(line)!!.destructured
                parts.add(Part(x.toInt(), m.toInt(), a.toInt(), s.toInt()))
            }
        }

        return workflows to parts
    }

    data class Workflow(val name: String, val rules: List<Rule>, val default: String)
    data class Rule(val field: String, val expression: String, val value: Int, val nextWorkflow: String)
    data class Part(val x: Int, val m: Int, val a: Int, val s: Int)
}
