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
        with(octopuses) {
            while ((part2 && values.any { it != 0 }) || rounds < 100) {
                rounds++
                keys.forEach { set(it, getValue(it) + 1) }
                while (values.any { it == 10 }) {
                    filterValues { it == 10 }.keys.forEach { full ->
                        flashes++
                        full.adjacent8().filter { getValue(it) in 0..9 }.forEach { set(it, getValue(it) + 1) }
                        set(full, getValue(full) + 1)
                    }
                }
                filterValues { it > 9 }.keys.forEach { set(it, 0) }
            }
        }

        return if (part2) rounds else flashes
    }

}

fun main() {
    print(Octopuses().run(part2 = true))
}