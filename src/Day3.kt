private class NumberIsland(map: List<String>, val points: ArrayList<Point>) {

    val value: Int
    private val mapSize: Pair<Int, Int>

    init {
        value = points.map { (x, y) -> map[y][x] }.joinToString(separator = "").toInt()
        mapSize = (map.first().length - 1) to (map.size - 1)
    }

    fun neighbours(): Set<Point> {
        return points.map { p -> p.neighbors() }.flatten().filter { p -> p !in points && p.inBound(0, mapSize.first, 0, mapSize.second) }.toSet()
    }

}

fun main() {
    val data = readFile(3)
    val numbers = arrayListOf<NumberIsland>()

    var actual = arrayListOf<Point>()
    data.forEachIndexed { i, row ->
        row.forEachIndexed { j, character ->
            if (character.isDigit()) {
                val coordinate = Point(j, i)

                if (actual.size > 0) {
                    val last = actual.last()

                    if (last.y == coordinate.y && last.x + 1 == coordinate.x) {
                        actual.add(coordinate)
                    } else {
                        numbers.add(NumberIsland(data, actual))
                        actual = arrayListOf(coordinate)
                    }
                } else {
                    actual.add(coordinate)
                }
            }
        }
    }
    numbers.add(NumberIsland(data, actual))

    val partNumbers = numbers.filter { number -> number.neighbours().any { (x, y) -> data[y][x] in "*/+-$#&%=@" } }

    println("Part 1: ${part1(partNumbers)}")
    println("Part 2: ${part2(data, partNumbers)}")
}


//Part 1
private fun part1(numbers: List<NumberIsland>): Int {
    return numbers.sumOf { it.value }
}


//Part 2
private fun part2(data: List<String>, numbers: List<NumberIsland>): Int {
    var sum = 0

    data.forEachIndexed { i, row ->
        row.forEachIndexed { j, character ->
            if (character == '*') {
                val numbersAround = numbers.filter { Point(j, i) in it.neighbours() }

                if (numbersAround.size == 2) {
                    sum += numbersAround[0].value * numbersAround[1].value
                }
            }
        }
    }

    return sum
}
