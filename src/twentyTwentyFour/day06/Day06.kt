package twentyTwentyFour.day06

import twentyTwentyFour.day04.Grid2D
import java.io.File
import kotlin.time.measureTime

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class Guard(var currentDirection: Direction) {}


class Grid2D(val grid: MutableList<CharArray>, val guard: Guard, var guardPosition: Pair<Int, Int>) {
    val minRow = 0
    val maxRow = grid.size - 1
    val minCol = 0;
    val maxCol = grid[0].size + 1
    var exited = false
    /**
     * moves guard 1 step in current direction
     * if a blocker is in front of the guard, rotate them 90 degrees to the right,
     * and move them 1 space in the new direction
     * the space that the guard was previously on should be marked as visited
     */
    fun moveGuard() {
        // check if the next space is a blocker
        val currentDirection = this.guard.currentDirection

        val nextSpace: Pair<Int, Int> = when (currentDirection) {
            Direction.UP -> Pair(this.guardPosition.first - 1, this.guardPosition.second)
            Direction.RIGHT -> Pair(this.guardPosition.first, this.guardPosition.second + 1)
            Direction.DOWN ->  Pair(this.guardPosition.first + 1, this.guardPosition.second)
            Direction.LEFT -> Pair(this.guardPosition.first, this.guardPosition.second - 1)
        }

        // mark the space the guard is in with an X before anything.
        grid[guardPosition.first][guardPosition.second] = 'X'

        // see if the next space is out-of-range
        if (isSpaceOutOfRange(nextSpace)) {
            // we're done! exit the guard
            exited = true
        } else if (isSpaceObstructed(nextSpace)) {
            // rotate the guard
            rotateGuard()
        } else {
            guardPosition = nextSpace
        }
    }

    fun isSpaceObstructed(space: Pair<Int, Int>): Boolean {
        return grid[space.first][space.second] == '#'
    }

    fun isSpaceOutOfRange(space: Pair<Int, Int>): Boolean {
        return space.first < minRow || space.first > maxRow || space.second < minCol || space.second > maxCol
    }

    fun rotateGuard() {
        val currentDirection = this.guard.currentDirection
        when (currentDirection) {
            Direction.UP -> guard.currentDirection = Direction.RIGHT
            Direction.RIGHT -> guard.currentDirection = Direction.DOWN
            Direction.DOWN ->  guard.currentDirection = Direction.LEFT
            Direction.LEFT -> guard.currentDirection = Direction.UP
        }
    }

    fun visitedSpaces(): Int {
        return grid.sumOf { l -> l.count { it == 'X' }}
    }

}

fun parseData(): List<CharArray> {
    return File("src/twentyTwentyFour/day06/data.txt").readLines().map { line -> line.toCharArray() }
}

fun findGuardPosition(grid: List<CharArray>, guardChar: Char = '^'): Pair<Int, Int> {
    for (row in grid.indices) {
        for (col in 0..<grid[0].size) {
            if (grid[row][col] == guardChar) {
                return Pair(row, col)
            }
        }
    }

    throw Error("Bad Data - NO Guard Position")
}

fun part1() {
    val grid = parseData()
    // find the guard & get their position
    val guardStartPosition = findGuardPosition(grid)
    val guard = Guard(Direction.UP) // always starts in up

    val grid2d = Grid2D(grid.toMutableList(), guard, guardStartPosition)

    while (!grid2d.exited) {
        grid2d.moveGuard()
    }

    println("part 1: " + grid2d.visitedSpaces())
}

fun main() {
    val t = measureTime { part1() }
    println("${t.inWholeMilliseconds}ms")
}