import utils.readInputBlock
import kotlin.math.max
import kotlin.math.sign

/** [https://adventofcode.com/2021/day/17] */
class Probes : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val pattern = Regex("target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)")
        val (lowerX, higherX, lowerY, higherY) = pattern.matchEntire(readInputBlock("17.txt"))!!.destructured
        val targetX = lowerX.toInt()..higherX.toInt()
        val targetY = lowerY.toInt()..higherY.toInt()
        val xBounds = (1..targetX.last + 1)
        val yBounds = (targetY.first..-targetY.first + 1)

        fun throwProbe(xInitialVelocity: Int, yInitialVelocity: Int): Int {
            var (x, y, xd, yd, maxY) = listOf(0, 0, xInitialVelocity, yInitialVelocity, Int.MIN_VALUE)
            while (y > targetY.first && x < targetX.last) {
                x += xd
                y += yd
                maxY = max(maxY, y)
                if (x in targetX && y in targetY) return maxY
                xd += -1 * xd.sign
                yd -= 1
            }

            return Int.MIN_VALUE
        }

        return if (part2)
            xBounds.sumOf { xd -> yBounds.count { yd -> throwProbe(xd, yd) > Int.MIN_VALUE } }
        else
            xBounds.maxOf { xd -> yBounds.maxOf { yd -> throwProbe(xd, yd) } }
    }
}

fun main() {
    print(Probes().run(part2 = true))
}