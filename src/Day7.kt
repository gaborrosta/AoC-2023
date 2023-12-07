private enum class Type(val count: String) {

    HighCard("11111"),

    OnePair("2111"),

    TwoPair("221"),

    Three("311"),

    FullHouse("32"),

    Four("41"),

    Five("5"),

}

private class CamelCard(val hand: String) {

    val type1: Type
    val type2: Type

    init {
        val typeAsString = hand.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
            .joinToString("") { it.second.toString() }
        type1 = Type.entries.find { t -> t.count == typeAsString } ?: Type.HighCard

        type2 = combinationWithRepetition(arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'), hand.count { it == 'J' })
            .map { map ->
                val js = map.entries.joinToString("") { (c, times) -> Array(times.toInt()) { c }.joinToString("") }
                val newHand = hand.replace("J", "") + js

                val type2AsString = newHand.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
                    .joinToString("") { it.second.toString() }

                Type.entries.find { t -> t.count == type2AsString } ?: Type.HighCard
            }.maxBy { it.ordinal }
    }

}

private fun createComparator(cardValue: (Char) -> Int, type: (CamelCard) -> Type) = Comparator<Pair<CamelCard, Int>> { (mine, _), (other, _) ->
    if (type(mine) == type(other)) {
        repeat(5) { i ->
            val mineValue = cardValue(mine.hand[i])
            val otherValue = cardValue(other.hand[i])

            if (mineValue != otherValue) {
                return@Comparator mineValue - otherValue
            }
        }

        0
    } else {
        type(mine).ordinal - type(other).ordinal
    }
}

fun main() {
    val data = readFile(7).map {
        val (card, bid) = it.split(" ", limit = 2)
        CamelCard(card.trim()) to bid.toInt()
    }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<Pair<CamelCard, Int>>): Long {
    return data.sortedWith(
        createComparator(
            cardValue = {
                return@createComparator when (it) {
                    'A' -> 13
                    'K' -> 12
                    'Q' -> 11
                    'J' -> 10
                    'T' -> 9
                    '9' -> 8
                    '8' -> 7
                    '7' -> 6
                    '6' -> 5
                    '5' -> 4
                    '4' -> 3
                    '3' -> 2
                    '2' -> 1
                    else -> throw Exception("Unknown card")
                }
            },
            type = { it.type1 },
        )
    ).foldIndexed(0L) { i, acc, (_, bid) -> acc + (i + 1) * bid }
}


//Part 2
private fun part2(data: List<Pair<CamelCard, Int>>): Long {
    return data.sortedWith(
        createComparator(
            cardValue = {
                return@createComparator when (it) {
                    'A' -> 13
                    'K' -> 12
                    'Q' -> 11
                    'T' -> 10
                    '9' -> 9
                    '8' -> 8
                    '7' -> 7
                    '6' -> 6
                    '5' -> 5
                    '4' -> 4
                    '3' -> 3
                    '2' -> 2
                    'J' -> 1
                    else -> throw Exception("Unknown card")
                }
            },
            type = { it.type2 },
        )
    ).foldIndexed(0L) { i, acc, (_, bid) -> acc + (i + 1) * bid }
}
