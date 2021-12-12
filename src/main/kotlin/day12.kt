import utils.readInputLines

/** [https://adventofcode.com/2021/day/12] */
class Paths : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val tunnels = readInputLines("12.txt").flatMap {
            val (source, target) = it.split("-")
            setOf(source to target, target to source)
        }

        fun isValidPath(next: String, path: List<String>, hasDoubleVisit: Boolean) =
            next.first().isUpperCase() ||
                    path.count { it == next } < (if (!part2 || hasDoubleVisit || next == "start") 1 else 2)

        fun discoverPaths(current: String = "start", path: List<String> = listOf(current)): Set<List<String>> {
            val hasDoubleVisit = if (part2) path.filter { it.first().isLowerCase() }
                .groupingBy { it }.eachCount().any { it.value == 2 } else false

            return if (current == "end") setOf(path) else
                tunnels.filter { it.first == current && isValidPath(it.second, path, hasDoubleVisit) }
                    .flatMap { discoverPaths(it.second, path + it.second) }.toSet()
        }

        return discoverPaths().size
    }
}

fun main() {
    print(Paths().run(part2 = true))
}