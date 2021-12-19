import utils.readInputBlock
import kotlin.math.pow

private typealias Point = Triple<Int, Int, Int>

/** [https://adventofcode.com/2021/day/19] */
class Day19 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val reports = readInputBlock("19.txt").split("\n\n").map { it.split("\n") }
        val scanners = reports.associate {
            it.first().split(" ")[2].toInt() to
                    it.drop(1).map { line ->
                        val (x, y, z) = line.split(",")
                        Triple(x.toInt(), y.toInt(), z.toInt())
                    }.toSet()
        }

        fun Point.variations(): Set<Point> {
            val (x, y, z) = this
            return setOf(
                Point(y, x, -z),
                Point(y, z, x),
                Point(y, -x, z),
                Point(y, -z, -x),

                Point(-z, y, x),
                Point(x, y, z),
                Point(z, y, -x),
                Point(-x, y, -z),

                Point(z, x, y),
                Point(-x, z, y),
                Point(-z, -x, y),
                Point(x, -z, y),

                Point(-x, z, -y),
                Point(-z, -x, -y),
                Point(x, -z, -y),
                Point(z, x, -y),

                Point(-y, x, z),
                Point(-y, -z, x),
                Point(-y, -x, -z),
                Point(-y, z, -x),

                Point(z, -y, x),
                Point(x, -y, -z),
                Point(-z, -y, -x),
                Point(-x, -y, z)
            )
        }

        fun Point.distanceTo(other: Point) =
            ((first - other.first).toDouble().pow(2) + (second - other.second).toDouble()
                .pow(2) + (third - other.third).toDouble().pow(2)).pow(1 / 2.0).toInt()

        val allScanners = scanners.mapValues { it.value.flatMap(Point::variations).toSet() }

        return allScanners
    }
}

fun main() {
    print(Day19().run(part2 = false))
}