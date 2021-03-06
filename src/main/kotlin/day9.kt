import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/9] */
class Basins : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val heights = readInputLines("9.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, c -> Coordinate(x, y) to c.digitToInt() }
        }.toMap().withDefault { 10 }
        val lowPoints = heights.keys.filter { point ->
            point.adjacent().values.all { heights.getValue(it) > heights.getValue(point) }
        }

        return if (part2)
            lowPoints.map { start ->
                val queue = mutableListOf(start)
                val basin = mutableSetOf<Coordinate>()

                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()
                    basin.add(current)
                    queue.addAll(current.adjacent().values.filter { it !in basin && heights.getValue(it) < 9 })
                }

                basin.size
            }.sortedDescending().take(3).reduce { a, b -> a * b }
        else
            lowPoints.sumOf { heights.getValue(it) + 1 }
    }
}

fun main() {
    print(Basins().run(part2 = true))
}