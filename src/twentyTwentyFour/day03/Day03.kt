package twentyTwentyFour.day03

import java.io.File

fun readParseData(): Int {
    val fileName = "src/twentyTwentyFour/day03/data.txt"
    val data = File(fileName).readText()

    // parse out mul instructions
    val regex = Regex("""mul\(\d{1,3},\d{1,3}\)""")

    // get the values out of the match results
    val results = regex.findAll(data)
    val mulStrings = results.map { it.value }

    // parse the mul() instructions
    val multiplyResults = mulStrings.map { ms ->
        multiplyInstruction(ms)
    }

    return multiplyResults.sum()
}

fun multiplyInstruction(instr: String): Int {
    return Regex("""\d{1,3}""").findAll(instr).map { it.value.toInt() }.reduce { acc, num -> acc * num }
}

fun readParseDataPart2(): Int {
    val fileName = "src/twentyTwentyFour/day03/data.txt"
    val data = File(fileName).readText()

    val regex = Regex("""do(?:n't)?|mul\(\d{1,3},\d{1,3}\)""")

    val results = regex.findAll(data)

    val instructions = results.map { r ->
        r.value
    }

    var enabled = true;
    var sum = 0;
    for (instruction in instructions) {
        when (instruction) {
            "do" -> enabled = true
            "don't" -> enabled = false
            else -> if (enabled) sum += multiplyInstruction(instruction)
        }
    }

    return sum;
}

fun part1() {
    val result = readParseData()
    println("part 1: $result")
}

fun part2() {
    val result = readParseDataPart2()
    println("part 2: $result");
}

fun main() {
    part1()
    part2()
}