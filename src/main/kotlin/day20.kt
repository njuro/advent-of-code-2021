import utils.*

/** [https://adventofcode.com/2021/day/20] */
class Pixels : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (encoding, input) = readInputBlock("20.txt").split("\n\n")
        var default = '.'
        var image = input.split("\n").flatMapIndexed { y, row ->
            row.mapIndexed { x, c -> Coordinate(x, y) to c }
        }.toMap().toMutableMap().withDefault { default }

        fun Coordinate.neighbours() = listOf(
            copy(x = x - 1, y = y - 1), copy(y = y - 1), copy(x = x + 1, y = y - 1),
            copy(x = x - 1), this, copy(x = x + 1),
            copy(x = x - 1, y = y + 1), copy(y = y + 1), copy(x = x + 1, y = y + 1)
        )

        fun Coordinate.enhanced() = encoding[
                neighbours().map { if (image.getValue(it) == '#') '1' else '0' }.joinToString("").toInt(2)
        ]

        repeat(if (part2) 50 else 2) {
            val updated = mutableMapOf<Coordinate, Char>()
            for (y in image.minY() - 1..image.maxY() + 1) {
                for (x in image.minX() - 1..image.maxX() + 1) {
                    val position = Coordinate(x, y)
                    updated[position] = position.enhanced()
                }
            }
            default = if (default == '.') '#' else '.'
            image = updated.withDefault { default }
        }

        return image.values.count { it == '#' }
    }
}

fun main() {
    print(Pixels().run(part2 = true))
}