import utils.readInputBlock

/** [https://adventofcode.com/2021/day/16] */
class Packets : AdventOfCodeTask {
    data class Packet(
        val version: Int,
        val type: Int,
        val value: Long,
        val size: Int,
        val subPackets: List<Packet> = emptyList()
    ) {
        val totalVersion: Int = version + subPackets.sumOf { it.totalVersion }
        val totalValue: Long = when (type) {
            0 -> subPackets.sumOf { it.totalValue }
            1 -> subPackets.fold(1L) { acc, packet -> acc * packet.totalValue }
            2 -> subPackets.minOf { it.totalValue }
            3 -> subPackets.maxOf { it.totalValue }
            4 -> value
            5 -> if (subPackets.first().totalValue > subPackets.last().totalValue) 1L else 0L
            6 -> if (subPackets.first().totalValue < subPackets.last().totalValue) 1L else 0L
            7 -> if (subPackets.first().totalValue == subPackets.last().totalValue) 1L else 0L
            else -> throw IllegalArgumentException("Unexpected type $type")
        }
    }

    override fun run(part2: Boolean): Any {
        val message = readInputBlock("16.txt").flatMap {
            it.digitToInt(16).toString(2).padStart(4, '0').toList()
        }
        var offset = 0

        fun parse(): Packet {
            var size = 0
            fun List<Char>.consume(length: Int) =
                subList(offset, offset + length).also { offset += length; size += length }

            fun List<Char>.toInt() = joinToString("").toInt(2)
            fun List<Char>.toLong() = joinToString("").toLong(2)

            val version = message.consume(3).toInt()
            val type = message.consume(3).toInt()

            if (type == 4) {
                val digits = mutableListOf<Char>()
                do {
                    val group = message.consume(5)
                    digits.addAll(group.drop(1))
                } while (group.first() == '1')

                return Packet(version, type, digits.toLong(), size)
            } else {
                val lengthType = message.consume(1).toInt()
                val subPackets = mutableListOf<Packet>()
                var subPacketsSize = 0
                if (lengthType == 0) {
                    val totalLength = message.consume(15).toInt()
                    while (subPacketsSize != totalLength) {
                        subPackets.add(parse().also { subPacketsSize += it.size })
                    }

                } else {
                    val subPacketCount = message.consume(11).toInt()
                    repeat(subPacketCount) { subPackets.add(parse().also { subPacketsSize += it.size }) }
                }
                size += subPacketsSize

                return Packet(version, type, -1, size, subPackets)
            }
        }

        return parse().let { if (part2) it.totalValue else it.totalVersion }
    }
}

fun main() {
    print(Packets().run(part2 = true))
}