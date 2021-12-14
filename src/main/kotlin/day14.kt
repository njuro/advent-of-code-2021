import utils.readInputBlock

/** [https://adventofcode.com/2021/day/14] */
class Polymers : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (template, transformationsBlock) = readInputBlock("14.txt").split("\n\n")
        val transformations = transformationsBlock.split("\n").associate {
            val (adjacent, insert) = it.split(" -> ")
            (adjacent.first() to adjacent.last()) to insert.first()
        }

        var current = template.zipWithNext().groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        repeat(if (part2) 40 else 10) {
            val next = mutableMapOf<Pair<Char, Char>, Long>().withDefault { 0L }
            current.forEach { (pair, count) ->
                val middle = transformations.getValue(pair)
                next[pair.first to middle] = next.getValue(pair.first to middle) + count
                next[middle to pair.second] = next.getValue(middle to pair.second) + count
            }
            current = next
        }

        val counter = mutableMapOf<Char, Long>().withDefault { 0L }
        current.forEach { (first, second), count ->
            counter[first] = counter.getValue(first) + count / 2
            counter[second] = counter.getValue(second) + count / 2
        }
        counter[template.last()] = counter.getValue(template.last()) - 1

        return counter.values.maxOf { it } - counter.values.minOf { it }
    }
}

fun main() {
    print(Polymers().run(part2 = true))
}