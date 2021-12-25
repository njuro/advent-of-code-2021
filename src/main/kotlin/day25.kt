import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/25] */
@ExperimentalStdlibApi
class Cucumbers : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val initial = readInputLines("25.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap()

        fun Map<Coordinate, Char>.transform(facing: Char, transformPosition: (Coordinate) -> Coordinate) =
            buildMap<Coordinate, Char> {
                putAll(this@transform)
                this@transform.filterValues { it == facing }.keys.forEach { prev ->
                    transformPosition(prev).takeIf { this@transform[it] == '.' }?.also { next ->
                        put(prev, '.')
                        put(next, facing)
                    }

                }
            }

        return generateSequence(initial to 0) { (current, steps) ->
            current
                .transform('>') { prev ->
                    prev.right().let { next -> if (next !in current) Coordinate(0, next.y) else next }
                }
                .transform('v') { prev ->
                    prev.down(offset = true).let { next -> if (next !in current) Coordinate(next.x, 0) else next }
                } to steps + 1
        }.zipWithNext().first { (prev, next) -> prev.first == next.first }.second.second
    }
}

@ExperimentalStdlibApi
fun main() {
    print(Cucumbers().run(part2 = true))
}