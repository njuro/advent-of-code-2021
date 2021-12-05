import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/5] */
class Vents : AdventOfCodeTask {

    data class Vent(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
        val minX = minOf(x1, x2)
        val maxX = maxOf(x1, x2)
        val minY = minOf(y1, y2)
        val maxY = maxOf(y1, y2)
        val isPrimaryDiagonal = (x2 > x1 && y2 > y1) || (x2 < x1 && y2 < y1)
        val isSecondaryDiagonal = (x2 > x1 && y2 < y1) || (x2 < x1 && y2 > y1)
        val isDiagonal = isPrimaryDiagonal || isSecondaryDiagonal

        operator fun contains(point: Coordinate): Boolean {
            val inRange = point.x in minX..maxX && point.y in minY..maxY
            return if (isDiagonal) {
                inRange && (
                        (isPrimaryDiagonal && point.x - minX == point.y - minY) ||
                                (isSecondaryDiagonal && point.x - minX == maxY - point.y)
                        )
            } else inRange
        }
    }

    override fun run(part2: Boolean): Any {
        val pattern = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")
        val vents = readInputLines("5.txt").map {
            val (x1, y1, x2, y2) = pattern.matchEntire(it)!!.destructured.toList().map(String::toInt)
            Vent(x1, y1, x2, y2)
        }

        return (0..vents.maxOf { it.maxY }).sumOf { y ->
            (0..vents.maxOf { it.maxX }).count { x ->
                vents.count { (part2 || !it.isDiagonal) && Coordinate(x, y) in it } > 1
            }
        }
    }

}

fun main() {
    print(Vents().run(part2 = true))
}