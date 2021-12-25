import utils.Coordinate
import utils.readInputLines
import java.util.*
import kotlin.collections.ArrayDeque

/** [https://adventofcode.com/2021/day/23] */
class Amphipods : AdventOfCodeTask {
    data class Amphipod(val type: Char, val position: Coordinate, val finished: Boolean = false)

    override fun run(part2: Boolean): Any {
        val cost = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
        val map = readInputLines("23.txt").toMutableList()
            .apply { if (part2) addAll(3, setOf("  #D#C#B#A#  ", "  #D#B#A#C#  ")) }
            .flatMapIndexed { y, row -> row.mapIndexed { x, c -> Coordinate(x, y) to c } }.toMap()
        val hallway = map.filter { it.key.y == 1 && it.value == '.' }.keys
        val checkpoints = hallway.filter { map[it.down(offset = true)] == '#' }
        val amphipods =
            map.filterValues { it.isUpperCase() }.map { Amphipod(type = it.value, position = it.key) }.toSet()
        val rooms = ('A'..'D').associateWith { type ->
            val x = 3 + (type - 'A') * 2
            (2 until (2 + if (part2) 4 else 2)).map { y -> Coordinate(x, y) }.toSet()
        }
        val allRooms = rooms.values.flatten().toSet()
        val allPlaces = allRooms + hallway

        val queue =
            PriorityQueue<Pair<Set<Amphipod>, Int>>(Comparator.comparing { it.second }).apply { add(amphipods to 0) }
        val seen = mutableSetOf<Set<Amphipod>>()
        while (!queue.peek().first.all(Amphipod::finished)) {
            val (state, energy) = queue.poll()
            seen.add(state)

            state.filterNot(Amphipod::finished).forEach { amphipod ->
                val (type, position) = amphipod
                val room = rooms.getValue(type)
                val destinations = if (position in checkpoints) room else (checkpoints + room)
                val paths = mutableSetOf<Pair<Coordinate, Int>>()
                val blocked = state.map { it.position }.toSet()
                val innerQueue = ArrayDeque<Pair<Coordinate, Int>>().apply { add(position to 0) }
                val visited = mutableSetOf<Coordinate>()
                while (innerQueue.isNotEmpty()) {
                    val (current, steps) = innerQueue.removeFirst()
                    visited.add(current)
                    if (steps > 0 && current in destinations) {
                        paths.add(current to steps)
                    }
                    current.adjacent().values.filter { it !in visited && it !in blocked && it in allPlaces }
                        .forEach { next ->
                            innerQueue.addLast(next to steps + 1)
                        }
                }

                paths.forEach { (destination, steps) ->
                    val updatedState = state.toMutableSet()
                    updatedState.remove(amphipod)
                    updatedState.add(amphipod.copy(position = destination, finished = destination in room))
                    if (updatedState !in seen) {
                        queue.offer(updatedState to energy + cost.getValue(type) * steps)
                    }
                }
            }

        }

        return queue.poll().second
    }
}

fun main() {
    print(Amphipods().run(part2 = false))
}