fun main() {
    val data = readFile(8)
    val instructions = data[0]
    val map = data.drop(2).associate {
        val line = it.split(" = (", ", ", ")").dropLast(1)
        line[0] to Pair(line[1], line[2])
    }

    println("Part 1: ${part1(instructions, map)}")
    println("Part 2: ${part2(instructions, map)}")
}


//Part 1
private fun part1(instructions: String, map: Map<String, Pair<String, String>>): Int {
    val size = instructions.length
    var index = 0
    var actual = "AAA"

    while (actual != "ZZZ") {
        actual = when (instructions[index % size]) {
            'R' -> map[actual]?.second ?: throw Exception("Not found")
            'L' -> map[actual]?.first ?: throw Exception("Not found")
            else -> throw Exception("Unknown direction")
        }

        index++
    }

    return index
}


//Part 2
private fun part2(instructions: String, map: Map<String, Pair<String, String>>): Long {
    val size = instructions.length
    val actuals = map.keys.filter { it.last() == 'A' }

    return lcm(actuals.map { start ->
        var index = 0
        var actual = start
        val result = arrayListOf<Int>()

        while (result.size < 2) {
            if (actual.last() == 'Z') {
                result.add(index)
            }

            actual = when (instructions[index % size]) {
                'R' -> map[actual]?.second ?: throw Exception("Not found")
                'L' -> map[actual]?.first ?: throw Exception("Not found")
                else -> throw Exception("Unknown direction")
            }

            index++
        }

        (result[1] - result[0]).toLong()
    })
}
