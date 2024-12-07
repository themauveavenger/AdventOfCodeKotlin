package twentyTwentyFour.day07

import java.io.File
import java.math.BigInteger
import java.util.ArrayDeque
import kotlin.time.measureTime

fun permutations(chars: List<Char>, size: Int): List<List<Char>> {
    if (size == 0) return listOf(emptyList())

    if (size == 1) return chars.map { listOf(it) }

    val result = mutableListOf<List<Char>>()

    for (char in chars) {
        val subPermutations = permutations(chars, size - 1)
        for (subPerm in subPermutations) {
            result.add(listOf(char) + subPerm)
        }
    }

    return result
}

class Equation(val testValue: BigInteger, val numbers: List<BigInteger>) {
    companion object {
        fun fromStr(line: String): Equation {
            val (tValue, nums) = line.split(":").map { it.trim() }
            val parsedNums = nums.split(Regex("\\s")).map { it.toBigInteger() }
            return Equation(tValue.toBigInteger(), parsedNums)
        }
    }
}

fun parseData(): List<Equation> {
    return File("src/twentyTwentyFour/day07/data.txt").readLines().map { Equation.fromStr(it) }
}

fun part1() {
    val data = parseData()
    val maxSize = data.maxOf { d -> d.numbers.size }
    val minSize = data.minOf { d -> d.numbers.size }

    // set up the map
    val operators = listOf('*', '+')
    val permutationsBySize: MutableMap<Int, List<List<Char>>> = mutableMapOf()
    for (s in minSize..maxSize) {
        permutationsBySize[s - 1] = permutations(operators, s - 1)
    }

    var sum = BigInteger.ZERO
    for (equation in data) {
        // get the permutations list we need
        val perms = permutationsBySize[equation.numbers.size - 1]
        if (perms != null) {
            for (p in perms) {
                // create a stack to pop off of
                val operationsStack = ArrayDeque(p)
                // perform the operations in order on the numbers
                val current = equation.numbers.first()
                val target = equation.numbers.drop(1).fold(current) { acc, v ->
                    when (operationsStack.removeFirst()) {
                        '*' -> acc * v
                        '+' -> acc + v
                        else -> throw Error("Bad data")
                    }
                }

                if (target == equation.testValue) {
                    sum += target
                    break
                }
            }
        }
    }

    println("part 1: $sum")
}

fun part2() {
    val data = parseData()
    val maxSize = data.maxOf { d -> d.numbers.size }
    val minSize = data.minOf { d -> d.numbers.size }

    // set up the map
    val operators = listOf('*', '+', '|')
    val permutationsBySize: MutableMap<Int, List<List<Char>>> = mutableMapOf()
    for (s in minSize..maxSize) {
        permutationsBySize[s - 1] = permutations(operators, s - 1)
    }

    var sum = BigInteger.ZERO

    for (equation in data) {
        // get the permutations list we need
        val perms = permutationsBySize[equation.numbers.size - 1]
        if (perms != null) {
            for (p in perms) {
                // create a stack to pop off of
                val operationsStack = ArrayDeque(p)
                // perform the operations in order on the numbers
                val current = equation.numbers.first()
                val target = equation.numbers.drop(1).fold(current) { acc, v ->
                    when (operationsStack.removeFirst()) {
                        '*' -> acc * v
                        '+' -> acc + v
                        '|' -> "$acc$v".toBigInteger()
                        else -> throw Error("Bad data")
                    }
                }

                if (target == equation.testValue) {
                    sum += target
                    break
                }
            }
        }
    }

    println("part 2: $sum")
}

fun main() {
    val time1 = measureTime { part1() }
    println(time1.inWholeMilliseconds)

    val time2 = measureTime { part2() }
    println(time2.inWholeMilliseconds)
}
