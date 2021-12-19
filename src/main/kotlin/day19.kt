import utils.readInputBlock
import kotlin.math.abs

private typealias PointTransformation = (Day19.Point) -> Day19.Point

/** [https://adventofcode.com/2021/day/19] */
class Day19 : AdventOfCodeTask {

    private val rotations: Set<PointTransformation> = setOf(
        { (x, y, z) -> Point(x, y, z) }, { (x, y, z) -> Point(-x, y, z) }, { (x, y, z) -> Point(x, -y, z) },
        { (x, y, z) -> Point(x, y, -z) }, { (x, y, z) -> Point(-x, -y, z) }, { (x, y, z) -> Point(x, -y, -z) },
        { (x, y, z) -> Point(-x, y, -z) }, { (x, y, z) -> Point(-x, -y, -z) })
    private val flips: Set<PointTransformation> = setOf(
        { (x, y, z) -> Point(x, y, z) }, { (x, y, z) -> Point(x, z, y) }, { (x, y, z) -> Point(y, x, z) },
        { (x, y, z) -> Point(y, z, x) }, { (x, y, z) -> Point(z, x, y) }, { (x, y, z) -> Point(z, y, x) })
    private val transformations =
        flips.flatMap { f -> rotations.map<PointTransformation, PointTransformation> { r -> { point -> r(f(point)) } } }

    data class Point(val x: Int, val y: Int, val z: Int) {
        operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
        operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)
        infix fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    override fun run(part2: Boolean): Any {
        val reports = readInputBlock("19.txt").split("\n\n").map { it.split("\n") }
        val data = reports.associate {
            it.first().split(" ")[2].toInt() to
                    it.drop(1).map { line ->
                        val (x, y, z) = line.split(",")
                        Point(x.toInt(), y.toInt(), z.toInt())
                    }
        }.mapValues { transformations.map { transformation -> it.value.map(transformation) } }

        fun List<Point>.computeVector(other: List<Point>) =
            flatMap { first -> other.map { second -> first - second } }
                .groupingBy { it }.eachCount().entries.firstOrNull { it.value >= 12 }?.key

        val beacons = mutableMapOf(0 to data.getValue(0).first())
        val scanners = mutableSetOf(Point(0, 0, 0))
        val processed = mutableSetOf<Int>()
        while (beacons.size != data.keys.size) {
            for ((scanner, sector) in beacons.filterKeys { it !in processed }) {
                for ((otherScanner, otherSectors) in data.filterKeys { it !in beacons }) {
                    for (otherSector in otherSectors) {
                        sector.computeVector(otherSector)?.run {
                            scanners.add(this)
                            beacons[otherScanner] = otherSector.map { point -> point + this }
                        }
                    }
                }
                processed.add(scanner)
            }
        }

        return if (part2) scanners.maxOf { first -> scanners.maxOf { second -> first distanceTo second } }
        else beacons.values.flatten().distinct().size
    }

}

fun main() {
    print(Day19().run(part2 = false))
}