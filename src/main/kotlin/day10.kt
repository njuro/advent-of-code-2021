import utils.readInputLines

/** [https://adventofcode.com/2021/day/10] */
class Chunks : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val pairs = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
        val scores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137, '(' to 1, '[' to 2, '{' to 3, '<' to 4)
        var errorScore = 0
        val completionScores = readInputLines("10.txt").mapNotNull { line ->
            val opened = line.fold(mutableListOf<Char>()) { buffer, c ->
                if (c in pairs.keys) {
                    buffer.apply { add(c) }
                } else {
                    if (c == pairs.getValue(buffer.removeLast())) buffer else {
                        errorScore += scores.getValue(c)
                        return@mapNotNull null
                    }
                }
            }

            opened.reversed().fold(0L) { score, next -> score * 5 + scores.getValue(next) }

        }

        return if (part2) completionScores.sorted()[completionScores.size / 2] else errorScore
    }
}

fun main() {
    print(Chunks().run(part2 = true))
}