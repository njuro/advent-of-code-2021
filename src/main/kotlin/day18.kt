import utils.readInputLines
import kotlin.math.ceil
import kotlin.math.floor

/** [https://adventofcode.com/2021/day/18] */
class Day18 : AdventOfCodeTask {
    sealed class SnailFish {
        companion object {
            fun parse(input: List<Char>): SnailFish {
                val buffer = mutableListOf<Char>()
                var index = 0
                var isLeft = true
                var left: SnailFish? = null
                var right: SnailFish? = null
                while (index < input.size) {
                    if (input[index] == '[') {
                        var nesting = 1
                        var endIndex = index + 1
                        while (true) {
                            if (input[endIndex] == '[') {
                                nesting++
                            }
                            if (input[endIndex] == ']') {
                                nesting--
                                if (nesting <= 0) {
                                    break
                                }
                            }
                            endIndex++
                        }
                        val sublist = input.subList(index + 1, endIndex + 1)
                        val subfish = SnailFish.parse(sublist)
                        if (isLeft) {
                            left = subfish
                        } else {
                            right = subfish
                        }
                        index += sublist.size + 1
                    } else if (input[index].isDigit()) {
                        buffer.add(input[index])
                        index++
                    } else if (input[index] == ',') {
                        if (left == null && buffer.isNotEmpty()) {
                            left = SnailLiteral(buffer.joinToString("").toInt())
                            buffer.clear()
                        }
                        isLeft = false
                        index++
                    } else if (input[index] == ']') {
                        if (right == null && buffer.isNotEmpty()) {
                            right = SnailLiteral(buffer.joinToString("").toInt())
                            buffer.clear()
                        }
                        isLeft = true
                        index++
                    }
                }

                if (buffer.isNotEmpty()) {
                    right = SnailLiteral(buffer.joinToString("").toInt())
                    buffer.clear()
                }

                return if (right != null) {
                    SnailPair(left!!, right)
                } else left!!
            }
        }

        var parent: SnailFish? = null

        abstract fun populate(newParent: SnailFish?)

        abstract fun reduced(level: Int = 1): Pair<SnailFish, Boolean>

        abstract fun magnitude(): Long

        fun add(other: SnailFish) = SnailPair(left = this, right = other).also { it.populate(null) }.reduced().first


    }

    data class SnailPair(var left: SnailFish, var right: SnailFish) : SnailFish() {
        override fun reduced(level: Int): Pair<SnailFish, Boolean> {
            var current = this
            while (true) {
                if (level == 4) {

                    var newParent = parent
                    return SnailLiteral(0).apply { this.parent = newParent } to true
                } else {
                    var newLeft = left.reduced(level + 1)
                    var newRight = right.reduced(level + 1)
                    var newParent = parent
                    current = SnailPair(newLeft.first, newRight.first).apply { this.parent = newParent }
                    if (!newLeft.second && !newRight.second) {
                        break
                    }
                }
            }

            return current to false
        }

        override fun populate(newParent: SnailFish?) {
            this.parent = newParent
            left.populate(this)
            right.populate(this)
        }

        override fun magnitude(): Long {
            return 3 * left.magnitude() + 2 * right.magnitude()
        }
    }

    data class SnailLiteral(var value: Int) : SnailFish() {
        override fun reduced(level: Int): Pair<SnailFish, Boolean> {
            if (value >= 10) {
                return SnailPair(
                    SnailLiteral(floor(value / 2.0).toInt()),
                    SnailLiteral(ceil(value / 2.0).toInt())
                ).apply {
                    this.parent = this@SnailLiteral.parent; this.left.parent = this; this.right.parent = this
                } to true
            }
            return this to false
        }

        override fun populate(newParent: SnailFish?) {
            this.parent = newParent
        }

        override fun magnitude(): Long {
            return value.toLong()
        }
    }

    override fun run(part2: Boolean): Any {
        val input =
            readInputLines("18.txt").map { SnailFish.parse(it.toList()).also { it.populate(null) }.reduced().first }

        return input.reduce { acc, snailFish -> acc.add(snailFish) }
    }
}

fun main() {
    print(Day18().run(part2 = false))
}