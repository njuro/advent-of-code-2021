package utils

fun lcm(a: Long, b: Long): Long {

    return a * (b / gcd(a, b))
}

fun gcd(a: Long, b: Long): Long {

    if (a == 0L) return b

    return gcd(b % a, a)
}
