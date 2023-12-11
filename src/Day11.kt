private fun calculateGalaxiesPositions(data: List<String>, repeat: Int): List<Point> {
    val extraRowsIndices = data.indicesOf { !it.contains("#") }
    val extraColumnsIndices = data.rotate().indicesOf { !it.contains("#") }

    val galaxies = arrayListOf<Point>()
    for (y in data.indices) {
        for (x in data[y].indices) {
            if (data[y][x] == '#') {
                galaxies.add(
                    Point(
                        x = x + extraColumnsIndices.count { it < x } * (repeat - 1),
                        y = y + extraRowsIndices.count { it < y } * (repeat - 1),
                    )
                )
            }
        }
    }

    return galaxies
}

fun main() {
    val data = readFile(11)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    val galaxies = calculateGalaxiesPositions(data, 2)

    return combinations(galaxies, 2).sumOf { (f, t) -> f.manhattan(t) } / 2
}


//Part 2
private fun part2(data: List<String>): Long {
    val galaxies = calculateGalaxiesPositions(data, 1000000)

    return combinations(galaxies, 2).sumOf { (f, t) -> f.manhattan(t).toLong() } / 2
}
