package twentyTwentyFour.day05

import java.io.File

class Rule(val before: Int, val after: Int) {
    fun isUpdateGood(update: List<Int>): Boolean {
        val beforeIndex = update.indexOf(before)
        val afterIndex = update.indexOf(after)

        // ignore missing values
        if (beforeIndex < 0 || afterIndex < 0) return true

        return beforeIndex < afterIndex
    }

    fun fixUpdate(update: List<Int>): List<Int> {
        val newList = update.toList().toMutableList()

        val beforeIndex = update.indexOf(before)
        val afterIndex = update.indexOf(after)

        // ignore missing values
        if (beforeIndex < 0 || afterIndex < 0) return newList.toList()

        if (beforeIndex > afterIndex) {
            // fix it, swap them
            newList[beforeIndex] = after
            newList[afterIndex] = before
        }

        return newList.toList()
    }

    companion object {
        fun fromStr(line: String): Rule {
            val (beforeStr, afterStr) = line.split("""|""").toList()
            return Rule(beforeStr.toInt(), afterStr.toInt())
        }
    }
}

fun parseRuleData(): List<Rule> {
    val data = File("src/twentyTwentyFour/day05/ruledata.txt").readLines()
    val rules = data.map { Rule.fromStr(it) }
    return rules
}

fun parseUpdateData(): List<List<Int>> {
    val data = File("src/twentyTwentyFour/day05/updatedata.txt").readLines()
    val updates = data.map { it.split(",").map { n -> n.toInt() } }
    return updates
}

fun part1() {
    val rules = parseRuleData()
    val updates = parseUpdateData()

    // each update must pass all rules
    val goodUpdates = updates.filter { update -> rules.all { rule -> rule.isUpdateGood(update) } }

//    println(goodUpdates.size)

    val sumMidNums = goodUpdates.sumOf { goodUpdate -> goodUpdate[goodUpdate.size / 2] }
    println("part 1: $sumMidNums")
}

fun allRulesPassing(rules: List<Rule>, updates: List<List<Int>>): Boolean {
    return updates.all { update -> rules.all { rule -> rule.isUpdateGood(update) } }
}

fun part2() {
    val rules = parseRuleData()
    val updates = parseUpdateData()

    val badUpdates = updates.filter { update -> !rules.all { rule -> rule.isUpdateGood(update) } }.toMutableList()

    do {
        for (badUpdateIndex in badUpdates.indices) {
            for (rule in rules) {
                badUpdates[badUpdateIndex] = rule.fixUpdate(badUpdates[badUpdateIndex])
            }
        }
    } while (!allRulesPassing(rules, badUpdates))

    val sumMidNums = badUpdates.sumOf { badUpdate -> badUpdate[badUpdate.size / 2] }
    println("part 2 $sumMidNums")
}

fun main() {
    part1()
    part2()
}