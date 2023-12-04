import kotlin.math.pow

private class Scratchcard(line: String) {

    val id: Int
    val mine: List<Int>
    val winners: List<Int>

    init {
        id = line.split(":")[0].removePrefix("Card").trim().toInt()
        winners = line.split("|")[0].split(":")[1].split(" ").mapNotNull { it.toIntOrNull() }
        mine = line.split("|")[1].split(" ").mapNotNull { it.toIntOrNull() }
    }

    fun countOfWonNumbers(): Int {
        return mine.count { it in winners }
    }

}

fun main() {
    val data = readFile(4).map { Scratchcard(it) }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<Scratchcard>): Int {
    return data.sumOf { scratchcard ->
        val count = scratchcard.countOfWonNumbers()

        2F.pow(count - 1).toInt()
    }
}


//Part 2
private fun part2(data: List<Scratchcard>): Int {
    val scratchcards = Array(data.size) { 0 }

    data.forEach { scratchcard ->
        val count = scratchcard.countOfWonNumbers()

        scratchcards[scratchcard.id - 1]++
        repeat(count) {
            scratchcards[scratchcard.id + it] += scratchcards[scratchcard.id - 1]
        }
    }

    return scratchcards.sum()
}
