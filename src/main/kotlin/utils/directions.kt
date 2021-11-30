package utils

enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    fun turnRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    fun turnOpposite() = turnRight().turnRight()
    fun turnLeft() = turnRight().turnRight().turnRight()
}