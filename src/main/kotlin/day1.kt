import utils.readInputLines

/** [https://adventofcode.com/2021/day/1] */
class Depths : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        return readInputLines("1.txt").map(String::toInt).let {
            if (part2) it.windowed(3) { it.sum() } else it
        }.zipWithNext().count { it.second > it.first }
    }
}

fun main() {
    print(Depths().run(part2 = true))
}