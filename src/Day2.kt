import kotlin.math.max

private class Game(line: String) {

    var id: Int
    var cubes: List<Triple<Int, Int, Int>>

    init {
        val segments = line.split(":", ";")

        id = segments[0].split(" ")[1].toInt()

        cubes = segments.drop(1).map { segment ->
            val game = segment.split(", ")

            val red = readNumberOfCubes(game, "red")
            val green = readNumberOfCubes(game, "green")
            val blue = readNumberOfCubes(game, "blue")

            Triple(red, green, blue)
        }
    }

    private fun readNumberOfCubes(game: List<String>, color: String): Int {
        val found = game.firstOrNull { it.contains(color) }

        return if (found == null) 0 else found.trim().split(" ")[0].toInt()
    }

    fun isItPossible(red: Int, green: Int, blue: Int): Int {
        return if (cubes.any { (r, g, b) -> r > red || g > green || b > blue }) 0 else id
    }

    fun minimumToBePossible(): Int {
        val (red, green, blue) = cubes.reduce { sum, now ->
            val (oldR, oldG, oldB) = sum
            val (nowR, nowG, nowB) = now

            Triple(max(oldR, nowR), max(oldG, nowG), max(oldB, nowB))
        }

        return red * green * blue
    }

}

fun main() {
    val data = readFile(2).map { Game(it) }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<Game>): Int {
    return data.sumOf { it.isItPossible(12, 13, 14) }
}


//Part 2
private fun part2(data: List<Game>): Int {
    return data.sumOf { it.minimumToBePossible() }
}
