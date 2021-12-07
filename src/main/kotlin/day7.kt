import utils.readInputBlock
import kotlin.math.abs

/** [https://adventofcode.com/2021/day/7] */
class Crabs : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val crabs = readInputBlock("7.txt").split(",").map(String::toInt)

        return (crabs.minOrNull()!!..crabs.maxOrNull()!!).minOf { target ->
            crabs.sumOf { source -> abs(target - source).let { if (part2) (it * (it + 1)) / 2 else it } }
        }
    }
}

fun main() {
    print(Crabs().run(part2 = true))
}