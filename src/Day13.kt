private fun findMirror(data: List<String>, shouldFixSmudge: Boolean): Int {
    var line = 1
    val height = data.size

    while (line < height) {
        var fixed = false
        var fine = true

        repeat(height) { i ->
            val before = line - i - 1
            val after = line + i

            if (before >= 0 && after < height) {
                if (shouldFixSmudge && data[before].zip(data[after]).count { (f, s) -> f != s } == 1 && !fixed) {
                    fixed = true
                } else {
                    fine = fine && data[before] == data[after]
                }
            }
        }

        if (fine && (!shouldFixSmudge || fixed)) {
            return line
        }

        line++
    }

    return 0
}

fun main() {
    val data = readFile(13).groupConsecutiveBy(keepFirstElement = false) { _, s -> s.isEmpty() }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<List<String>>): Long {
    return data.sumOf { grid ->
        val horizontal = findMirror(grid, shouldFixSmudge = false)
        val vertical = findMirror(grid.rotateStrings(), shouldFixSmudge = false)

        vertical + 100L * horizontal
    }
}


//Part 2
private fun part2(data: List<List<String>>): Long {
    return data.sumOf { grid ->
        val horizontal = findMirror(grid, shouldFixSmudge = true)
        val vertical = findMirror(grid.rotateStrings(), shouldFixSmudge = true)

        vertical + 100L * horizontal
    }
}
