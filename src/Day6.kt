fun main() {
    val data = readFile(6).map { it.removePrefix("Time:").removePrefix("Distance:") }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    return data.asSequence().map { it.split(" ").mapNotNull { n -> n.toIntOrNull() } }
        .zipWithNext().map { (t, d) -> t.zip(d) }.flatten().fold(1) { acc, (t, d) ->
            var ok = 0

            for (i in 1..t) {
                val distance = (t - i) * i

                if (distance > d) {
                    ok++
                }
            }

            acc * ok
        }
}


//Part 2
private fun part2(data: List<String>): Long {
    val (t, d) = data.map { it.replace(" ", "").toLong() }
    var ok = 0L

    for (i in 1L..t) {
        val distance = (t - i) * i

        if (distance > d) {
            ok++
        }
    }

    return ok
}
