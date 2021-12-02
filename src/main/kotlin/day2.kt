import utils.readInputLines

/** [https://adventofcode.com/2021/day/2] */
class Commands : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        var (depth, position, aim) = listOf(0, 0, 0)
        readInputLines("2.txt").forEach { line ->
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
        }

        return depth * position
    }
}

fun main() {
    print(Commands().run(part2 = true))
}