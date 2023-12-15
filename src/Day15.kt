private fun hash(input: String): Int {
    var result = 0

    input.forEach { ch ->
        result += ch.code
        result *= 17
        result %= 256
    }

    return result
}

fun main() {
    val data = readFile(15)[0].split(",")

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    return data.sumOf { hash(it) }
}


//Part 2
private fun part2(data: List<String>): Int {
    val boxes = Array(256) { ArrayList<Pair<String, Int>>() }

    data.forEach { line ->
        val operation = line.split("=", "-")[0]
        val box = hash(operation)
        val isDash = line.endsWith("-")
        val focalLength = if (isDash) 0 else line.split("=")[1].toInt()

        if (isDash) {
            boxes[box].removeIf { (op, _) -> op == operation }
        } else {
            val index = boxes[box].indexOfFirst { it.first == operation }

            if (index == -1) {
                boxes[box].add(operation to focalLength)
            } else {
                boxes[box][index] = operation to focalLength
            }
        }
    }

    return boxes.withIndex().sumOf { (index, b) ->
        b.withIndex().sumOf { (position, pair) ->
            (index + 1) * (position + 1) * pair.second
        }
    }
}
