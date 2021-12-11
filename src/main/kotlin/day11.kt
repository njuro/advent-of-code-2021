import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/11] */
class Octopuses : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val octopuses = readInputLines("11.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, c -> Coordinate(x, y) to c.digitToInt() }
        }.toMap().toMutableMap().withDefault { -1 }

        var flashes = 0
        var rounds = 0
        while ((part2 && octopuses.values.any { it != 0 }) || rounds < 100) {
            rounds++
            octopuses.keys.forEach { octopuses[it] = octopuses[it]!! + 1 }
            while (octopuses.values.any { it == 10 }) {
                octopuses.filter { it.value == 10 }.keys.forEach { full ->
                    flashes++
                    full.adjacent8()
                        .filter { octopuses.getValue(it) in 0..9 }
                        .forEach { octopuses[it] = octopuses[it]!! + 1 }
                    octopuses[full] = octopuses[full]!! + 1
                }
            }
            octopuses.filter { it.value > 9 }.keys.forEach { octopuses[it] = 0 }
        }

        return if (part2) rounds else flashes
    }

}

fun main() {
    print(Octopuses().run(part2 = true))
}