import kotlinx.serialization.json.*
import utils.readInputLines
import kotlin.math.ceil
import kotlin.math.floor

/** [https://adventofcode.com/2021/day/18] */
class Snails : AdventOfCodeTask {
    enum class Side { L, R }
    sealed class SnailFish {
        companion object {
            fun parse(raw: String) = parse(Json.parseToJsonElement(raw))

            private fun parse(input: JsonElement): SnailFish = when (input) {
                is JsonArray -> SnailPair(parse(input.first()), parse(input.last()))
                is JsonPrimitive -> SnailLiteral(input.int)
                else -> throw IllegalArgumentException()
            }
        }

        var parent: SnailPair? = null

        open fun initParent(parent: SnailPair? = null) = run { this.parent = parent }
        open fun explode(side: Side, level: Int = 1): Boolean = false
        open fun split(side: Side): Boolean = false

        abstract fun pushLeft(value: Int)
        abstract fun pushRight(value: Int)
        abstract fun magnitude(): Int
        abstract fun copy(): SnailFish
    }

    data class SnailPair(var left: SnailFish, var right: SnailFish) : SnailFish() {
        operator fun plus(other: SnailFish) = SnailPair(this, other).apply(::initParent).reduced()

        private fun reduced(): SnailPair {
            while (left.explode(Side.L) || right.explode(Side.R) || split(Side.L)) {
                initParent()
            }
            return this
        }

        override fun initParent(parent: SnailPair?) = run {
            this.parent = parent
            this.left.initParent(this)
            this.right.initParent(this)
        }

        override fun explode(side: Side, level: Int): Boolean {
            if (level == 4) {
                val reduced = SnailLiteral(0).apply { parent = this@SnailPair.parent }
                pushLeft((left as SnailLiteral).value)
                pushRight((right as SnailLiteral).value)
                if (side == Side.L) parent!!.left = reduced else parent!!.right = reduced
                return true
            }

            return left.explode(Side.L, level + 1) || right.explode(Side.R, level + 1)
        }

        override fun split(side: Side) = left.split(Side.L) || right.split(Side.R)

        override fun pushLeft(value: Int) {
            val current = this.parent ?: return
            if (current.left === this) {
                current.pushLeft(value)
                return
            }
            if (current.left is SnailPair) {
                var leaf: SnailFish = (current.left as SnailPair).right
                while (leaf is SnailPair) leaf = leaf.right
                leaf.pushRight(value)
                return
            }
            current.left.pushRight(value)
        }

        override fun pushRight(value: Int) {
            val current = this.parent ?: return
            if (current.right === this) {
                current.pushRight(value)
                return
            }
            if (current.right is SnailPair) {
                var leaf: SnailFish = (current.right as SnailPair).left
                while (leaf is SnailPair) leaf = leaf.left
                leaf.pushLeft(value)
                return
            }
            current.right.pushLeft(value)
        }

        override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
        override fun copy() = SnailPair(left.copy(), right.copy()).apply(SnailFish::initParent)
        override fun toString() = "[$left,$right]"
    }

    data class SnailLiteral(var value: Int) : SnailFish() {
        override fun split(side: Side): Boolean {
            if (value >= 10) {
                val reduced = SnailPair(
                    SnailLiteral(floor(value / 2.0).toInt()),
                    SnailLiteral(ceil(value / 2.0).toInt())
                ).apply { parent = this@SnailLiteral.parent; left.parent = this; right.parent = this }
                if (side == Side.L) parent!!.left = reduced else parent!!.right = reduced
                return true
            }

            return false
        }

        override fun pushLeft(value: Int) = run { this.value += value }
        override fun pushRight(value: Int) = run { this.value += value }
        override fun magnitude() = value
        override fun copy() = SnailLiteral(value)
        override fun toString() = "$value"

    }

    override fun run(part2: Boolean): Any {
        val input = readInputLines("18.txt")
            .map { (SnailFish.parse(it) as SnailPair).apply(SnailPair::initParent) }

        return if (part2) input.maxOf { first ->
            input.filter { it !== first }.maxOf { second -> (first.copy() + second.copy()).magnitude() }
        } else input.reduce(SnailPair::plus).magnitude()
    }
}

fun main() {
    print(Snails().run(part2 = true))
}