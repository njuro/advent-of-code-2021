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

        var parent: SnailPair? = null

        abstract fun populate(newParent: SnailPair?)

        abstract fun reduced(level: Int): Pair<SnailFish, Boolean>

        abstract fun explodeLeft(value: Int)

        abstract fun explodeRight(value: Int)

        abstract fun magnitude(): Long

        fun add(other: SnailFish) = SnailPair(left = this, right = other).also { it.populate(null) }.reduced()


        fun reduced(): SnailFish {
            var current = this
            while (true) {
                println(current)
                val (newValue, wasReduced) = current.reduced(0).also { it.first.populate(null) }
                if (!wasReduced) {
                    break
                }
                println(newValue)
                println()
                current = newValue
            }
            return current
        }
    }

    data class SnailPair(var left: SnailFish, var right: SnailFish) : SnailFish() {
        override fun reduced(level: Int): Pair<SnailFish, Boolean> {
            if (level == 4) {
                println("EXPLODE $this")
                explodeLeft((this.left as SnailLiteral).value)
                explodeRight((this.right as SnailLiteral).value)
                var newParent = parent
                return SnailLiteral(0).apply { this.parent = newParent } to true
            } else {
                var newParent = parent
                var newLeft = left.reduced(level + 1)
                if (newLeft.second) {
                    return SnailPair(newLeft.first, right).apply { this.parent = newParent } to true
                }
                var newRight = right.reduced(level + 1)
                if (newRight.second) {
                    return SnailPair(left, newRight.first).apply { this.parent = newParent } to true
                }

                return this to false
            }
        }

        override fun explodeLeft(value: Int) {
            val current = this.parent ?: return
            if (current.left === this) {
                current.explodeLeft(value)
                return
            }
            if (current.left is SnailPair) {
                var ok: SnailFish = (current.left as SnailPair).right
                while (ok is SnailPair) ok = ok.right
                ok.explodeRight(value)
                return
            }
            current.left.explodeRight(value)
        }

        override fun explodeRight(value: Int) {
            val current = this.parent ?: return
            if (current.right === this) {
                current.explodeRight(value)
                return
            }
            if (current.right is SnailPair) {
                var ok: SnailFish = (current.right as SnailPair).left
                while (ok is SnailPair) ok = ok.left
                ok.explodeLeft(value)
                return
            }
            current.right.explodeLeft(value)
        }

        override fun populate(newParent: SnailPair?) {
            this.parent = newParent
            left.populate(this)
            right.populate(this)
        }

        override fun magnitude(): Long {
            return 3 * left.magnitude() + 2 * right.magnitude()
        }

        override fun toString(): String {
            return "[$left,$right]"
        }
    }

    data class SnailLiteral(var value: Int) : SnailFish() {
        override fun reduced(level: Int): Pair<SnailFish, Boolean> {
            if (value >= 10) {
                println("SPLIT $this")
                return SnailPair(
                    SnailLiteral(floor(value / 2.0).toInt()),
                    SnailLiteral(ceil(value / 2.0).toInt())
                ).apply {
                    this.parent = this@SnailLiteral.parent; this.left.parent = this; this.right.parent = this
                } to true
            }
            return this to false
        }

        override fun explodeLeft(value: Int) {
            this.value += value
        }

        override fun explodeRight(value: Int) {
            this.value += value
        }

        override fun populate(newParent: SnailPair?) {
            this.parent = newParent
        }

        override fun magnitude(): Long {
            return value.toLong()
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    override fun run(part2: Boolean): Any {
        val input =
            readInputLines("18.txt").map { SnailFish.parse(it.toList()).also { it.populate(null) }.reduced() }

        return input.reduce { acc, snailFish -> acc.add(snailFish) }
    }
}

fun main() {
    print(Day18().run(part2 = false))
}