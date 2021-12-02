import utils.readInputLines

/** [https://adventofcode.com/2021/day/2] */
class Commands : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        return readInputLines("2.txt").fold(Triple(0, 0, 0)) { data, line ->
            var (position, depth, aim) = data
            val (command, value) = line.split(" ")
            with(value.toInt()) {
                when (command) {
                    "forward" -> {
                        position += this
                        if (part2) depth += aim * this
                    }
                    "up" -> if (part2) aim -= this else depth -= this
                    "down" -> if (part2) aim += this else depth += this
                }
            }
            Triple(position, depth, aim)
        }.let { it.first * it.second }
    }
}

fun main() {
    print(Commands().run(part2 = true))
}