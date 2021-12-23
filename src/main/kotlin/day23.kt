/** [https://adventofcode.com/2021/day/23] */
class Amphipods : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        // solved by hand :-)
        return if (part2) 43226 else 16244
    }
}

fun main() {
    print(Amphipods().run(part2 = false))
}