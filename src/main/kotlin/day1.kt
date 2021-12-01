import utils.readInputLines

/** [https://adventofcode.com/2021/day/1] */
class Depths : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val depths = readInputLines("1.txt").map(String::toInt)

        return if (part2)
            depths.windowed(size = 3, step = 1, partialWindows = false)
                .zipWithNext()
                .count { it.second.sum() > it.first.sum() }
        else depths.zipWithNext().count { it.second > it.first }
    }
}

fun main() {
    print(Depths().run(part2 = true))
}