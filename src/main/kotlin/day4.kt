import utils.readInputBlock
import java.util.regex.Pattern

private typealias Board = List<List<Int>>

/** [https://adventofcode.com/2021/day/4] */
class Bingo : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        val input = readInputBlock("4.txt").split("\n\n")
        val pool = input.first().split(",").map(String::toInt).toMutableList()
        val boards: MutableSet<Board> = input.drop(1).map { board ->
            board.split("\n").map { row ->
                row.trim().split(Pattern.compile("\\s+")).map(String::toInt)
            }
        }.toMutableSet()
        val drawn = mutableSetOf<Int>()

        fun Board.hasWinningRow() = any { row -> row.all { it in drawn } }
        fun Board.hasWinningColumn() = first().indices.any { column -> map { it[column] }.all { it in drawn } }
        fun Board.isWinner() = hasWinningRow() || hasWinningColumn()
        fun Board.score(lastNumber: Int) = flatten().filter { it !in drawn }.sum() * lastNumber

        var score = -1
        while (score < 0 && pool.isNotEmpty()) {
            val current = pool.removeFirst()
            drawn.add(current)

            boards.filter(Board::isWinner).forEach { winner ->
                boards.remove(winner)
                if (!part2 || boards.isEmpty()) {
                    score = winner.score(current)
                    return@forEach
                }
            }
        }

        return score
    }
}

fun main() {
    print(Bingo().run(part2 = true))
}