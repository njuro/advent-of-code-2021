import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AdventOfCodeTasksTest {

    @Test
    fun day01() {
        runTaskTest(Depths(), 1655, 1683)
    }

    @Test
    fun day02() {
        runTaskTest(Commands(), 1480518, 1282809906)
    }

    @Test
    fun day03() {
        runTaskTest(Reports(), 1307354, 482500)
    }

    @Test
    fun day04() {
        runTaskTest(Bingo(), 23177, 6804)
    }

    @Test
    fun day05() {
        runTaskTest(Vents(), 4745, 18442)
    }

    @Test
    fun day06() {
        runTaskTest(Fish(), 386536L, 1732821262171L)
    }

    @Test
    fun day07() {
        runTaskTest(Crabs(), 355592, 101618069)
    }

    @Test
    fun day08() {
        runTaskTest(Digits(), 274, 1012089)
    }

    @Test
    fun day09() {
        runTaskTest(Basins(), 528, 920448)
    }

    @Test
    fun day10() {
        runTaskTest(Chunks(), 442131, 3646451424L)
    }

    @Test
    fun day11() {
        runTaskTest(Octopuses(), 1741, 440)
    }

    @Test
    fun day12() {
        runTaskTest(Paths(), 5252, 147784)
    }

    @Test
    fun day13() {
        runTaskTest(
            Dots(), 607, """
                                     ██  ███  ████ █    ███  ████ ████ █   
                                    █  █ █  █    █ █    █  █ █       █ █   
                                    █    █  █   █  █    █  █ ███    █  █   
                                    █    ███   █   █    ███  █     █   █   
                                    █  █ █    █    █    █    █    █    █   
                                     ██  █    ████ ████ █    █    ████ ████
                                  """.trimIndent()
        )
    }

    @Test
    fun day14() {
        runTaskTest(Polymers(), 2937L, 3390034818249L)
    }

    @Test
    fun day15() {
        runTaskTest(Risks(), 447, 2825)
    }

    @Test
    fun day16() {
        runTaskTest(Packets(), 1014, 1922490999789L)
    }

    @Test
    fun day17() {
        runTaskTest(Probes(), 15931, 2555)
    }

    @Test
    fun day18() {
        runTaskTest(Snails(), 3756, 4585)
    }

    @Test
    fun day19() {
        runTaskTest(Scanners(), 330, 9634)
    }

    @Test
    fun day20() {
        runTaskTest(Pixels(), 5065, 14790)
    }

    @Test
    fun day21() {
        runTaskTest(Dices(), 506466, 632979211251440L)
    }

    @Test
    fun day22() {
        runTaskTest(Cubes(), 567496L, 1355961721298916L)
    }

    @Test
    fun day23() {
        runTaskTest(Amphipods(), 16244, 43226)
    }

    @Test
    fun day24() {
        runTaskTest(Program(), 45989929946199L, 11912814611156L)
    }

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}