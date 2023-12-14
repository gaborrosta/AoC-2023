fun main() {
    val data = readFile(14).map { it.toList() }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<List<Char>>): Int {
    val height = data.size

    val after = arrayListOf<ArrayList<Char>>()
    data.forEach { after.add(ArrayList(it)) }

    val rocks = data.rotate().map { it.indicesOf { ch -> ch == '#' } }

    data.forEachIndexed { y, row ->
        row.forEachIndexed { x, ch ->
            if (ch == 'O' && y > 0) {
                var newY = rocks[x].firstOrNull { height - it - 1 < y }?.let { height - it - 1 } ?: 0
                while (after[newY][x] != '.' && newY < y) {
                    newY++
                }

                after[newY][x] = 'O'
                if (newY != y) {
                    after[y][x] = '.'
                }
            }
        }
    }

    return after.foldIndexed(0) { y, soFar, row -> row.count { it == 'O' } * (height - y) + soFar }
}


//Part 2
private fun part2(data: List<List<Char>>): Long {
    val height = data.size
    val width = data[0].size

    var before = arrayListOf<ArrayList<Char>>()
    val after = arrayListOf<ArrayList<Char>>()
    data.forEach {
        before.add(ArrayList(it))
        after.add(ArrayList(it))
    }

    val northRocks = data.rotate().map { it.indicesOf { ch -> ch == '#' } }
    val southRocks = northRocks.map { it.reversed().map { index -> height - index - 1 } }
    val eastRocks = data.map { it.indicesOf { ch -> ch == '#' } }
    val westRocks = eastRocks.map { it.reversed().map { index -> width - index - 1 } }

    val history = arrayListOf<Long>()

    repeat(1_000) {
        //North
        before.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                if (ch == 'O' && y > 0) {
                    var newY = northRocks[x].firstOrNull { height - it - 1 < y }?.let { height - it - 1 } ?: 0
                    while (after[newY][x] != '.' && newY < y) {
                        newY++
                    }

                    after[newY][x] = 'O'
                    if (newY != y) {
                        after[y][x] = '.'
                    }
                }
            }
        }

        before = after

        //West
        before.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                if (ch == 'O' && x > 0) {
                    var newX = westRocks[y].firstOrNull { width - it - 1 < x }?.let { width - it - 1 } ?: 0
                    while (after[y][newX] != '.' && newX < x) {
                        newX++
                    }

                    after[y][newX] = 'O'
                    if (newX != x) {
                        after[y][x] = '.'
                    }
                }
            }
        }

        before = after

        //South
        before.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                if (ch == 'O' && y < height - 1) {
                    var newY = southRocks[x].firstOrNull { it > y } ?: (height - 1)
                    while (after[newY][x] != '.' && newY > y) {
                        newY--
                    }

                    after[newY][x] = 'O'
                    if (newY != y) {
                        after[y][x] = '.'
                    }
                }
            }
        }

        before = after

        //East
        before.forEachIndexed { y, row ->
            row.withIndex().reversed().forEach { (x, ch) ->
                if (ch == 'O' && x < width - 1) {
                    var newX = eastRocks[y].firstOrNull { it > x } ?: (width - 1)
                    while (after[y][newX] != '.' && newX > x) {
                        newX--
                    }

                    after[y][newX] = 'O'
                    if (newX != x) {
                        after[y][x] = '.'
                    }
                }
            }
        }

        before = after

        history.add(after.foldIndexed(0) { y, soFar, row -> row.count { it == 'O' } * (height - y) + soFar })
    }

    return recognisePatternInList(history, 1_000_000_000 - 1)
}
