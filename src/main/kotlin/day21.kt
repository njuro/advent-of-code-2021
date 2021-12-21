import utils.readInputLines
import java.lang.Integer.min

/** [https://adventofcode.com/2021/day/21] */
class Dices : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        var (player1, player2) = readInputLines("21.txt").map { it.split(" ").last().toInt() }
        fun Int.advance(roll: Int, limit: Int = 10) = (this - 1 + roll) % limit + 1

        return if (part2) {
            val cache = mutableMapOf<List<Int>, Pair<Long, Long>>()
            fun game(player1: Int, player2: Int, score1: Int = 0, score2: Int = 0, turn: Int = 1): Pair<Long, Long> {
                val state = listOf(player1, player2, score1, score2, turn)
                if (state in cache) return cache[state]!!
                if (score1 >= 21) return 1L to 0L
                if (score2 >= 21) return 0L to 1L
                var wins = 0L to 0L

                (1..3).forEach { r1 ->
                    (1..3).forEach { r2 ->
                        (1..3).forEach { r3 ->
                            val roll = r1 + r2 + r3
                            val nextPos = if (turn == 1) player1.advance(roll) else player2.advance(roll)
                            val nextWins = if (turn == 1)
                                game(nextPos, player2, score1 + nextPos, score2, 2)
                            else
                                game(player1, nextPos, score1, score2 + nextPos, 1)
                            wins = (wins.first + nextWins.first) to (wins.second + nextWins.second)
                        }
                    }
                }

                cache[state] = wins
                return wins
            }

            val (wins1, wins2) = game(player1, player2)
            if (wins1 > wins2) wins1 else wins2
        } else {
            var (dice, diceThrows, score1, score2) = listOf(1, 0, 0, 0)
            fun roll(): Int {
                var totalRoll = 0
                repeat(3) { totalRoll += dice; dice = dice.advance(1, 100); diceThrows++ }
                return totalRoll
            }
            while (true) {
                player1 = player1.advance(roll()); score1 += player1
                if (score1 >= 1000) break

                player2 = player2.advance(roll()); score2 += player2
                if (score2 >= 1000) break
            }

            min(score1, score2) * diceThrows
        }
    }
}

fun main() {
    print(Dices().run(part2 = false))
}