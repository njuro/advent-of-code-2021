import utils.readInputLines

/** [https://adventofcode.com/2021/day/8] */
class Digits : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val converter = mapOf(
            setOf('a', 'b', 'c', 'e', 'f', 'g') to 0,
            setOf('c', 'f') to 1,
            setOf('a', 'c', 'd', 'e', 'g') to 2,
            setOf('a', 'c', 'd', 'f', 'g') to 3,
            setOf('b', 'c', 'd', 'f') to 4,
            setOf('a', 'b', 'd', 'f', 'g') to 5,
            setOf('a', 'b', 'd', 'e', 'f', 'g') to 6,
            setOf('a', 'c', 'f') to 7,
            setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') to 8,
            setOf('a', 'b', 'c', 'd', 'f', 'g') to 9
        )

        return readInputLines("8.txt").sumOf { entry ->
            val (signals, digits) = entry.split(" | ").map { it.split(" ").map(String::toSet) }
            val mappings = mutableMapOf<Char, Char>()
            mappings['b'] = ('a'..'g').first { c -> signals.count { c in it } == 6 }
            mappings['e'] = ('a'..'g').first { c -> signals.count { c in it } == 4 }
            mappings['f'] = ('a'..'g').first { c -> signals.count { c in it } == 9 }
            mappings['c'] = signals.first { it.size == 2 }.minus(mappings['f']!!).first()
            mappings['d'] =
                signals.first { it.size == 4 }.minus(setOf(mappings['b']!!, mappings['c']!!, mappings['f']!!)).first()
            mappings['a'] = signals.first { it.size == 3 }.minus(setOf(mappings['c']!!, mappings['f']!!)).first()
            mappings['g'] = ('a'..'g').first { it !in mappings.values }
            val reversedMappings = mappings.entries.associateBy({ it.value }) { it.key }

            digits.map { digit -> converter[digit.map { reversedMappings[it]!! }.toSet()] }.let { result ->
                if (part2) result.joinToString("").toInt() else result.count { it in setOf(1, 4, 7, 8) }
            }
        }
    }
}

fun main() {
    print(Digits().run(part2 = true))
}