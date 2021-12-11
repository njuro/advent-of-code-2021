package utils

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

data class Coordinate(val x: Int, val y: Int) {

    fun distanceToCenter(): Int {
        return distanceTo(Coordinate(0, 0))
    }

    fun distanceTo(other: Coordinate): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun angleTo(other: Coordinate, inDegrees: Boolean = false): Double {
        val radians = atan2(
            (other.y - y).toDouble(),
            (other.x - x).toDouble()
        )

        return if (inDegrees) (radians * 180 / PI + 90).let { if (it < 0) it + 360 else it } else radians
    }

    fun up(delta: Int = 1, offset: Boolean = false): Coordinate {
        return if (offset) down(delta, false) else copy(y = y + delta)
    }

    fun right(delta: Int = 1): Coordinate {
        return copy(x = x + delta)
    }

    fun down(delta: Int = 1, offset: Boolean = false): Coordinate {
        return if (offset) up(delta, false) else copy(y = y - delta)
    }

    fun left(delta: Int = 1): Coordinate {
        return copy(x = x - delta)
    }

    fun move(direction: Direction, offset: Boolean = false): Coordinate {
        return when (direction) {
            Direction.UP -> up(offset = offset)
            Direction.RIGHT -> right()
            Direction.DOWN -> down(offset = offset)
            Direction.LEFT -> left()
        }
    }

    fun adjacent(offset: Boolean = false): Map<Direction, Coordinate> {
        return Direction.values().associateWith { move(it, offset) }
    }

    fun adjacent8() = adjacent().values + setOf(
        copy(x = x + 1, y = y + 1),
        copy(x = x - 1, y = y - 1),
        copy(x = x - 1, y = y + 1),
        copy(x = x + 1, y = y - 1)
    )

    operator fun plus(other: Coordinate): Coordinate {
        return Coordinate(x + other.x, y + other.y)
    }

    operator fun minus(other: Coordinate): Coordinate {
        return Coordinate(x - other.x, y - other.y)
    }
}

fun Map<Coordinate, Char>.minX(): Int {
    return minByOrNull { it.key.x }!!.key.x
}

fun Map<Coordinate, Char>.maxX(): Int {
    return maxByOrNull { it.key.x }!!.key.x
}

fun Map<Coordinate, Char>.minY(): Int {
    return minByOrNull { it.key.y }!!.key.y
}

fun Map<Coordinate, Char>.maxY(): Int {
    return maxByOrNull { it.key.y }!!.key.y
}

fun Map<Coordinate, Char>.toStringRepresentation(offsetCoordinates: Boolean = false, separator: String = " "): String {
    val map = StringBuilder()

    val yRange = if (offsetCoordinates) minY()..maxY() else maxY() downTo minY()
    for (y in yRange) {
        for (x in minX()..maxX()) {
            map.append(getValue(Coordinate(x, y)))
            map.append(separator)
        }
        map.append('\n')
    }

    return map.toString().trimEnd()
}
