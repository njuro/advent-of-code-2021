import utils.readInputLines

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
    }

    data class SnailPair(val left: SnailFish, val right: SnailFish) : SnailFish()
    data class SnailLiteral(val value: Int) : SnailFish()

    override fun run(part2: Boolean): Any {
        val input = readInputLines("18.txt").map { SnailFish.parse(it.toList()) }

        return -1
    }
}

fun main() {
    print(Day18().run(part2 = false))
}