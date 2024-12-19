package utils

enum class Directions(val x: Int, val y: Int) {
    NORTH(0, 1),
    SOUTH(0, -1),
    EAST(1, 0),
    WEST(-1, 0);

    operator fun component1(): Int {
        return x
    }

    operator fun component2(): Int {
        return y

    }
}
