import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2021/day/15] */
class Day15 : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val chitons = readInputLines("15.txt").flatMapIndexed { y, row ->
            row.mapIndexed { x, c -> Coordinate(x, y) to c.digitToInt() }
        }.toMap().toMutableMap().withDefault { -1 }
        val start = Coordinate(0, 0)
        val end = Coordinate(chitons.keys.maxOf { it.x }, chitons.keys.maxOf { it.y })

        val unvisited = chitons.keys.toMutableSet()
        val distances = chitons.keys.associateWith { Int.MAX_VALUE }.toMutableMap()
        distances[start] = 0

        var current = start
        while (unvisited.isNotEmpty()) {
            current.adjacent().values.filter { it in unvisited }.forEach { neighbour ->
                val newDistance = distances.getValue(current) + chitons.getValue(neighbour)
                if (newDistance < distances.getValue(neighbour)) {
                    distances[neighbour] = newDistance
                }
            }
            if (current == end) {
                break
            }
            unvisited.remove(current)
            current = unvisited.minByOrNull { distances.getValue(it) }!!
        }

        return distances.getValue(end)
    }
}

fun main() {
    print(Day15().run(part2 = false))
}