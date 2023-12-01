fun main() {
    val data = readFile(1)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    return data.sumOf { line ->
        var number = line.firstOrNull { ch -> ch.isDigit() }?.toString() ?: "0"
        if (line.count { ch -> ch.isDigit() } != 0) {
            number += line.last { ch -> ch.isDigit() }
        }

        number.toInt()
    }
}


//Part 2
private fun part2(data: List<String>): Int {
    return part1(data.map {
        var line = it
        var index = 0

        while (index <= line.lastIndex) {
            while (index <= line.lastIndex && line[index].isDigit()) {
                index++
            }

            var number = 1
            var found = false
            while (number < 10 && !found) {
                val result = recogniseText(line, index, number)
                line = result.first
                found = result.second

                number++
            }

            index++
        }

        line
    })
}

private fun recogniseText(line: String, index: Int, number: Int): Pair<String, Boolean> {
    val text = when (number) {
        1 -> "one"
        2 -> "two"
        3 -> "three"
        4 -> "four"
        5 -> "five"
        6 -> "six"
        7 -> "seven"
        8 -> "eight"
        9 -> "nine"
        else -> throw Exception("Wrong number")
    }

    return if (line.drop(index).startsWith(text)) {
        line.take(index) + number + line.drop(index + 1) to true
    } else {
        line to false
    }
}
