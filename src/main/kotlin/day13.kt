import utils.Coordinate
import utils.readInputBlock
import utils.toStringRepresentation

/** [https://adventofcode.com/2021/day/13] */
class Dots : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (dotsBlock, instructionsBlock) = readInputBlock("13.txt").split("\n\n")
        val dots = dotsBlock.split("\n").map {
            val (x, y) = it.split(",")
            Coordinate(x.toInt(), y.toInt())
        }.toSet()
        val instructions = instructionsBlock.split("\n").map {
            val (axis, value) = Regex("fold along ([xy])=(\\d+)").matchEntire(it)!!.destructured
            axis.first() to value.toInt()
        }

        fun Set<Coordinate>.fold(instructions: List<Pair<Char, Int>>) =
            instructions.fold(this) { current, (axis, value) ->
                current.map {
                    if (axis == 'x' && it.x > value) Coordinate(value - (it.x - value), it.y)
                    else if (axis == 'y' && it.y > value) Coordinate(it.x, value - (it.y - value))
                    else it
                }.toSet()
            }

        return if (part2)
            dots.fold(instructions).associateWith { '█' }.withDefault { ' ' }
                .toStringRepresentation(offsetCoordinates = true, separator = "")
        else
            dots.fold(listOf(instructions.first())).size

    }
}

fun main() {
    print(Dots().run(part2 = true))
}