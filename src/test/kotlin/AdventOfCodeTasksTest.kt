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

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}