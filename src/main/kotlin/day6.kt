import utils.readInputBlock

/** [https://adventofcode.com/2021/day/6] */
class Fish : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val timers = readInputBlock("6.txt")
            .split(",")
            .map(String::toInt)
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMutableMap()
            .withDefault { 0L }

        repeat(if (part2) 256 else 80) {
            val finished = timers.getValue(0)
            (1..8).forEach { time ->
                timers[time - 1] = timers.getValue(time)
            }
            timers[8] = finished
            timers[6] = timers.getValue(6) + finished
        }

        return timers.values.sum()
    }
}

fun main() {
    print(Fish().run(part2 = true))
}