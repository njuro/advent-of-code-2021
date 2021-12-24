import kotlin.math.pow

/** [https://adventofcode.com/2021/day/24] */
class Program : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {

        val powers = (0..13).associateWith { (10.0).pow(it) }
        var input = if (part2) 11912810000000 else 45989929999999 // bit of cheating here, so it doesn't take so long
        fun Long.digit(n: Int) = (this / powers[13 - n]!! % 10).toLong()

        fun valid(): Boolean {
            //1
            var z = 14L + input.digit(0)

            //2
            z = (z * 26) + input.digit(1) + 6

            //3
            z = (z * 26) + input.digit(2) + 6

            //4
            z = (z * 26) + input.digit(3) + 13

            // 5
            if (z % 26 - 12 != input.digit(4)) return false
            z /= 26

            // 6
            z = z * 26 + input.digit(5) + 8

            // 7
            if (z % 26 - 15 != input.digit(6)) return false
            z /= 26

            // 8
            z = z * 26 + input.digit(7) + 10

            // 9
            z = z * 26 + input.digit(8) + 8

            // 10
            if (z % 26 - 13 != input.digit(9)) return false
            z /= 26

            // 11
            if (z % 26 - 13 != input.digit(10)) return false
            z /= 26

            // 12
            if (z % 26 - 14 != input.digit(11)) return false
            z /= 26

            // 13
            if (z % 26 - 2 != input.digit(12)) return false
            z /= 26

            // 14
            if (z % 26 - 9 != input.digit(13)) return false
            z /= 26


            return z == 0L
        }

        while (if (part2) input < 99_999_999_999_999 else input > 11_111_111_111_111) {
            if (valid() && "0" !in input.toString()) {
                return input
            }
            if (part2) input++ else input--
        }

        throw IllegalStateException()
    }
}

fun main() {
    print(Program().run(part2 = true))
}