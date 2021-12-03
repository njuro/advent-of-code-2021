import utils.readInputLines

/** [https://adventofcode.com/2021/day/3] */
class Reports : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val input = readInputLines("3.txt")
        var gamma = ""
        var epsilon = ""

        if (part2) {
            val gammaCandidates = input.toMutableList()
            val epsilonCandidates = input.toMutableList()

            input.first().indices.forEach { index ->
                gammaCandidates.removeIf { it[index] != gammaCandidates.leastAndMostCommonAt(index).first }
                epsilonCandidates.removeIf { it[index] != epsilonCandidates.leastAndMostCommonAt(index).second }
            }

            gamma = gammaCandidates.first()
            epsilon = epsilonCandidates.first()

        } else {
            input.first().indices.forEach { index ->
                val (least, most) = input.leastAndMostCommonAt(index)
                gamma += least
                epsilon += most
            }
        }

        return gamma.toInt(2) * epsilon.toInt(2)
    }

    private fun List<String>.leastAndMostCommonAt(index: Int) =
        map { it[index] }
            .groupingBy { it }.eachCount().entries.sortedWith(compareBy({ it.value }, { it.key }))
            .map { it.key }
            .let { it.first() to it.last() }
}

fun main() {
    print(Reports().run(part2 = true))
}