package `2023`

import println
import readInput

fun main() {
    check(Day19().part1(readInput("2023/Day19_Test")) == 19114L).also { "Check Part1 passed".println() }
    Day19().part1(readInput("2023/Day19")).println()

    check(Day19().part2(readInput("2023/Day19_Test")) == 952408144115L).also { "Check Part2 passed".println() }
    Day19().part2(readInput("2023/Day19")).println()
}

class Day19 {
    fun part1(input: List<String>): Long {
        val (workflows, parts) = parseInput(input)

        return parts
            .filter { part -> processPart(workflows, "in", part) }
            .sumOf { (x, m, a, s) -> x.toLong() + m + a + s }
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

    fun part2(input: List<String>): Long {
        TODO()
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
                workflows[key] = Workflow(rules, default)
            } else {
                val (x, m, a, s) = partRegex.find(line)!!.destructured
                parts.add(Part(x.toInt(), m.toInt(), a.toInt(), s.toInt()))
            }
        }

        return workflows to parts
    }

    data class Workflow(val rules: List<Rule>, val default: String)
    data class Rule(val field: String, val expression: String, val value: Int, val nextWorkflow: String)
    data class Part(val x: Int, val m: Int, val a: Int, val s: Int)
}
