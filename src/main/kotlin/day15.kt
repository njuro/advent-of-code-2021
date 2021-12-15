import utils.Coordinate
import utils.readInputLines
import java.util.*

/** [https://adventofcode.com/2021/day/15] */
class Risks : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val original = readInputLines("15.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, c -> Coordinate(x, y) to c.digitToInt() }
        }.toMap()
        val width = original.keys.maxOf { it.x } + 1
        val height = original.keys.maxOf { it.y } + 1

        fun Int.increase(value: Int) = (this + value).let { if (it > 9) (it % 10) + 1 else it }
        fun Map<Coordinate, Int>.enlarged() =
            toMutableMap().apply {
                putAll((1..4).flatMap {
                    map { (coordinate, value) ->
                        Coordinate(coordinate.x + it * width, coordinate.y) to value.increase(it)
                    }
                })
                putAll((1..4).flatMap {
                    map { (coordinate, value) ->
                        Coordinate(coordinate.x, coordinate.y + it * height) to value.increase(it)
                    }
                })
            }

        val map = if (part2) original.enlarged() else original
        val start = Coordinate(0, 0)
        val end = Coordinate(map.keys.maxOf { it.x }, map.keys.maxOf { it.y })

        val queue = PriorityQueue<Pair<Coordinate, Int>>(Comparator.comparing { it.second }).apply { add(start to 0) }
        val seen = mutableSetOf<Coordinate>()
        while (queue.isNotEmpty() && queue.peek().first != end) {
            val (position, cost) = queue.poll()
            position.adjacent().values.filter { it in map && it !in seen }.forEach { neighbour ->
                seen.add(neighbour)
                queue.add(neighbour to cost + map.getValue(neighbour))
            }

        }

        return queue.poll().second
    }
}

fun main() {
    print(Risks().run(part2 = true))
}