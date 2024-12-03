package twentyTwentyFour.day02

import java.io.File
import kotlin.math.abs

fun isSafe(report: List<Int>): Boolean {
    val orderSafe = report == report.sortedDescending() || report == report.sorted()

    val pairs = report.zipWithNext();
    val differencesCount = pairs.count { level ->
        val difference = abs(level.first - level.second)
        difference in 1..3
    }

    return orderSafe && differencesCount == pairs.size;
}

fun isSafeWithDampener(report: List<Int>): Boolean {
    var indexToRemove = 0;
    do {
        val modifiedReport = report.toMutableList();
        if (indexToRemove > -1 && indexToRemove < report.size) {
            modifiedReport.removeAt(indexToRemove);
        }

        val orderSafe = modifiedReport == modifiedReport.sortedDescending() || modifiedReport == modifiedReport.sorted()

        val pairs = modifiedReport.zipWithNext();
        val differencesCount = pairs.count { level ->
            val difference = abs(level.first - level.second)
            difference in 1..3
        }

        val safe = orderSafe && differencesCount == pairs.size;
        if (safe) {
            return safe;
        } else {
            indexToRemove += 1;
        }
    } while (indexToRemove < report.size)

    return false;
}

fun readParseData(): List<List<Int>> {
    val fileName = "src/twentyTwentyFour/day02/data.txt";
    val data = File(fileName).readLines().map { it.split(Regex("\\s")) };

    return data.map { line ->
        line.map { it.toInt(10) }
    }
}

fun part1() {
    val reports = readParseData();

    val safeCount = reports.count { isSafe(it) }

    println("part 1 $safeCount");
}

fun part2() {
    val reports = readParseData();

    val safeCount = reports.count { isSafeWithDampener(it) }

    println("part 2 $safeCount");
}

fun main() {
    part1();
    part2();
}
