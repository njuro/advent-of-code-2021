import kotlin.math.pow

/** [https://adventofcode.com/2021/day/24] */
class Program : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {

        val powers = (0..13).associateWith { (10.0).pow(it) }
        var input = if (part2) 11_111_111_111_111 else 99_999_999_999_999
        fun Long.digit(n: Int) = (input / powers[13 - n]!! % 10).toLong()

        fun process(): Long {
            var x = 0L
            var z = 0L

            //1
            z = 14L + input.digit(0)

            //2
            z = (z * 26) + input.digit(1) + 6

            //3
            z = (z * 26) + input.digit(2) + 6

            //4
            z = (z * 26) + input.digit(3) + 13

            // 5
            x = if (z % 26 - 12 == input.digit(4)) 0 else 1
            z = (z / 26) * (x * 25 + 1) + (input.digit(4) + 8) * x

            // 6
            z = z * 26 + input.digit(5) + 8

            // 7
            x = if (z % 26 - 15 == input.digit(6)) 0L else 1L
            z = (z / 26) * (x * 25 + 1) + (input.digit(6) + 7) * x

            // 8
            z = z * 26 + input.digit(7) + 10

            // 9
            z = z * 26 + input.digit(8) + 8

            // 10
            x = if (z % 26 - 13 == input.digit(9)) 0 else 1
            z = (z / 26) * (x * 25 + 1) + (input.digit(9) + 12) * x

            // 11
            x = if (z % 26 - 13 == input.digit(10)) 0 else 1
            z = (z / 26) * (x * 25 + 1) + (input.digit(10) + 10) * x

            // 12
            x = if (z % 26 - 14 == input.digit(11)) 0 else 1
            z = (z / 26) * (x * 25 + 1) + (input.digit(11) + 8) * x

            // 13
            x = if (z % 26 - 2 == input.digit(12)) 0 else 1
            z = (z / 26) * (x * 25 + 1) + (input.digit(12) + 8) * x

            // 14
            x = if (z % 26 - 9 == input.digit(13)) 0 else 1
            z = (z / 26) * (x * 25 + 1) + (input.digit(13) + 7) * x


            return z
        }

        while (if (part2) input < 99_999_999_999_999 else input > 11_111_111_111_111) {
            if (process() == 0L) {
                return input
            }
            if (part2) input-- else input++
        }

        throw IllegalStateException()
    }
}

fun main() {
    print(Program().run(part2 = true))
}