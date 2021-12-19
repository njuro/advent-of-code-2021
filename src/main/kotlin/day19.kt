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
                    }
        }.mapValues { transformations.map { trans -> it.value.map(trans) } }

        fun versionsOverlap(version1: List<Point>, version2: List<Point>): List<Point> {
            val deltas = mutableMapOf<Point, Int>().withDefault { 0 }
            version1.forEach { point1 ->
                version2.forEach { point2 ->
                    val delta =
                        Point(point1.first - point2.first, point1.second - point2.second, point1.third - point2.third)
                    deltas[delta] = deltas.getValue(delta) + 1
                }
            }

            if (deltas.any { it.value >= 12 }) {
                val delta = deltas.filter { it.value >= 12 }.keys.first()
                return version1.filter {
                    Point(
                        it.first - delta.first,
                        it.second - delta.second,
                        it.third - delta.third
                    ) in version2
                }
            }

            return emptyList()
        }

        fun scannersOverlap(scanner1: List<List<Point>>, scanner2: List<List<Point>>): List<Point> {
            for (version1 in scanner1) {
                for (version2 in scanner2) {
                    val result = versionsOverlap(version1, version2)
                    if (result.isNotEmpty()) {
                        return result
                    }
                }
            }
            return emptyList()
        }



        return scannersOverlap(scanners[3]!!, scanners[4]!!)
    }

    private val transformations: List<(Point) -> Point> = listOf(
        { (x, y, z) -> Point(y, x, -z) },
        { (x, y, z) -> Point(y, z, x) },
        { (x, y, z) -> Point(y, -x, z) },
        { (x, y, z) -> Point(y, -z, -x) },
        { (x, y, z) -> Point(-z, y, x) },
        { (x, y, z) -> Point(x, y, z) },
        { (x, y, z) -> Point(z, y, -x) },
        { (x, y, z) -> Point(-x, y, -z) },
        { (x, y, z) -> Point(z, x, y) },
        { (x, y, z) -> Point(-x, z, y) },
        { (x, y, z) -> Point(-z, -x, y) },
        { (x, y, z) -> Point(x, -z, y) },
        { (x, y, z) -> Point(-x, z, -y) },
        { (x, y, z) -> Point(-z, -x, -y) },
        { (x, y, z) -> Point(x, -z, -y) },
        { (x, y, z) -> Point(z, x, -y) },
        { (x, y, z) -> Point(-y, x, z) },
        { (x, y, z) -> Point(-y, -z, x) },
        { (x, y, z) -> Point(-y, -x, -z) },
        { (x, y, z) -> Point(-y, z, -x) },
        { (x, y, z) -> Point(z, -y, x) },
        { (x, y, z) -> Point(x, -y, -z) },
        { (x, y, z) -> Point(-z, -y, -x) },
        { (x, y, z) -> Point(-x, -y, z) }
    )

    private fun Point.distanceTo(other: Point) =
        ((first - other.first).toDouble().pow(2) + (second - other.second).toDouble()
            .pow(2) + (third - other.third).toDouble().pow(2)).pow(1 / 2.0).toInt()
}

fun main() {
    print(Day19().run(part2 = false))
}