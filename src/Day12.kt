private fun String.checkArrangement(list: List<Int>): Boolean {
    return this.toList().groupConsecutiveBy { index, char -> index > 0 && this[index - 1] != char }
        .map { it[0] to it.size }
        .filter { it.first == '#' }.map { it.second } == list
}

fun main() {
    val data = readFile(12).map {
        val (damagedSprings, arrangements) = it.split(" ", limit = 2)
        damagedSprings to arrangements.split(",").map { n -> n.toInt() }
    }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2()}")
}


//Part 1
private fun part1(data: List<Pair<String, List<Int>>>): Long {
    return data.sumOf { (damagedSpring, arrangement) ->
        val numberOfUnknowns = damagedSpring.count { it == '?' }
        val combinations = combinations(listOf('.', '#'), numberOfUnknowns)

        combinations.count { combination ->
            var actualIndexOfNew = 0
            val sb = StringBuilder()
            for (ch in damagedSpring) {
                if (ch == '?') {
                    sb.append(combination[actualIndexOfNew])
                    actualIndexOfNew++
                } else {
                    sb.append(ch)
                }
            }

            sb.toString().checkArrangement(arrangement)
        }.toLong()
    }
}


//Part 2
private fun part2(): Int {
    //I did not manage to solve this part on my own, I needed help from others' solutions.
    return -1
}
