package utils

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val dir = Paths.get("src", "main")
    val template = Files.readString(dir.resolve("kotlin").resolve("template.kt"))
    for (day in 1..25) {
        val newFile = dir.resolve("kotlin").resolve("day$day.kt").toFile()
        if (newFile.exists()) {
            continue
        }
        val content = template.replace("INSERT_DAY", day.toString())
        newFile.writeText(content)
        dir.resolve("resources").resolve("$day.txt").toFile().createNewFile()
    }
}