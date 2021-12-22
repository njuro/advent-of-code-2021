import utils.readInputLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/** [https://adventofcode.com/2021/day/22] */
class Cubes : AdventOfCodeTask {
    data class Cuboid(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange, val positive: Boolean) {
        companion object {
            private val smallRange: IntRange = -50..50
            private fun IntRange.small() = first in smallRange && last in smallRange
            private fun IntRange.size() = abs(last - first) + 1L
            private infix fun IntRange.intersects(other: IntRange) = first in other || other.first in this
        }

        val small = xRange.small() && yRange.small() && zRange.small()
        val volume = (xRange.size() * yRange.size() * zRange.size()).let { if (positive) it else it * -1 }

        private fun intersects(other: Cuboid) =
            xRange intersects other.xRange && yRange intersects other.yRange && zRange intersects other.zRange

        fun intersect(other: Cuboid): Cuboid? {
            if (!intersects(other)) return null
            val xRange = max(xRange.first, other.xRange.first)..min(xRange.last, other.xRange.last)
            val yRange = max(yRange.first, other.yRange.first)..min(yRange.last, other.yRange.last)
            val zRange = max(zRange.first, other.zRange.first)..min(zRange.last, other.zRange.last)
            return Cuboid(xRange, yRange, zRange, positive = !other.positive)
        }
    }

    override fun run(part2: Boolean): Any {
        val pattern = Regex("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)")
        return readInputLines("22.txt").map {
            val (command, minX, maxX, minY, maxY, minZ, maxZ) = pattern.matchEntire(it)!!.destructured
            Cuboid(
                minX.toInt()..maxX.toInt(),
                minY.toInt()..maxY.toInt(),
                minZ.toInt()..maxZ.toInt(),
                command == "on"
            )
        }.filter { part2 || it.small }.fold(emptyList<Cuboid>()) { cuboids, cuboid ->
            cuboids + cuboids.mapNotNull { cuboid.intersect(it) }.let { if (cuboid.positive) it + cuboid else it }
        }.sumOf(Cuboid::volume)
    }
}

fun main() {
    print(Cubes().run(part2 = true))
}