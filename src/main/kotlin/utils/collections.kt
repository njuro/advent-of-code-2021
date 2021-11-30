package utils

fun <T> List<T>.permutations(): Set<List<T>> {
    if (isEmpty()) {
        return setOf()
    }

    if (size == 1) {
        return setOf(this)
    }

    val result = mutableSetOf<List<T>>()
    for (element in this) {
        val copy = toMutableList().also { it.remove(element) }
        for (perms in copy.permutations()) {
            result += mutableListOf(element) + perms
        }
    }

    return result
}