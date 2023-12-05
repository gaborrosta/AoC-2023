private class RangeWithConverter(val range: ClosedRange<Long>, val difference: Long)

private class Converter(lines: List<String>) {

    private val modifier: ArrayList<Pair<RangeWithConverter, RangeWithConverter>> = arrayListOf()

    init {
        lines.forEach {
            val (destinationStart, sourceStart, length) = it.split(" ", limit = 3).map { s -> s.toLong() }
            modifier.add(
                Pair(
                    RangeWithConverter(range = sourceStart..<sourceStart + length, difference = destinationStart - sourceStart),
                    RangeWithConverter(range = destinationStart..<destinationStart + length, difference = sourceStart - destinationStart)
                )
            )
        }
    }

    fun convert(from: Long): Long {
        return modifier.firstOrNull { from in it.first.range }?.first?.difference?.plus(from) ?: from
    }

    fun convertBack(to: Long): Long {
        return modifier.firstOrNull { to in it.second.range }?.second?.difference?.plus(to) ?: to
    }

}

fun main() {
    val data = readFile(5)
    val seedsLine = data[0].split(": ")[1].split(" ").map { it.toLong() }
    val converters = listOf(
        Converter(data.drop(3).takeWhile { it.isNotEmpty() }),
        Converter(data.dropWhile { !it.startsWith("soil-to-fertilizer") }.drop(1).takeWhile { it.isNotEmpty() }),
        Converter(data.dropWhile { !it.startsWith("fertilizer-to-water") }.drop(1).takeWhile { it.isNotEmpty() }),
        Converter(data.dropWhile { !it.startsWith("water-to-light") }.drop(1).takeWhile { it.isNotEmpty() }),
        Converter(data.dropWhile { !it.startsWith("light-to-temperature") }.drop(1).takeWhile { it.isNotEmpty() }),
        Converter(data.dropWhile { !it.startsWith("temperature-to-humidity") }.drop(1).takeWhile { it.isNotEmpty() }),
        Converter(data.dropWhile { !it.startsWith("humidity-to-location") }.drop(1).takeWhile { it.isNotEmpty() }),
    )

    println("Part 1: ${part1(seedsLine, converters)}")
    println("Part 2: ${part2(seedsLine, converters)}")
}


//Part 1
private fun part1(seedsLine: List<Long>, converters: List<Converter>): Long {
    return seedsLine.minOf { seed ->
        var now = seed
        repeat(converters.size) {
            now = converters[it].convert(now)
        }

        now
    }
}


//Part 2
private fun part2(seedsLine: List<Long>, converters: List<Converter>): Long {
    val seeds = seedsLine.windowed(2, 2).map { (it.first()..<(it.first() + it.last() - 1)) }

    var actual = 0L
    var found = false
    while (!found) {
        var from = actual
        repeat(converters.size) {
            from = converters[converters.size - 1 - it].convertBack(from)
        }

        if (seeds.any { from in it }) {
            found = true
        } else {
            actual++
        }
    }

    return actual
}
