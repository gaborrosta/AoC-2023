fun main() {
    val data = readFile(9).map { line -> line.split(" ").map { it.toInt() } }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<List<Int>>): Int {
    return data.sumOf { line ->
        val sequences = arrayListOf(line.toMutableList())

        while (sequences.last().any { it != 0 }) {
            sequences.add(sequences.last().zipWithNext().map { (f, s) -> s - f }.toMutableList())
        }

        val depth = sequences.size
        repeat(depth - 1) {
            val level = depth - it - 2

            sequences[level].add(sequences[level + 1].last() + sequences[level].last())
        }

        sequences[0].last()
    }
}


//Part 2
private fun part2(data: List<List<Int>>): Int {
    return data.sumOf { line ->
        val sequences = arrayListOf(line.toMutableList())

        while (sequences.last().any { it != 0 }) {
            sequences.add(sequences.last().zipWithNext().map { (f, s) -> s - f }.toMutableList())
        }

        val depth = sequences.size
        repeat(depth - 1) {
            val level = depth - it - 2

            sequences[level].add(0, sequences[level].first() - sequences[level + 1].first())
        }

        sequences[0].first()
    }
}
