private fun solve(data: List<String>, start: Point, startDirection: Point): Int {
    val height = data.size
    val width = data[0].length

    val active = hashSetOf<Point>()
    val visited = arrayListOf<Pair<Point, Point>>()

    val beams = arrayListOf(start to startDirection)
    while (beams.isNotEmpty()) {
        val (beam, direction) = beams.removeAt(0)
        val next = beam + direction

        if (next.inBound(width - 1, height - 1)) {
            visited.add(beam to direction)
            active.add(next)

            when (data[next.y][next.x]) {
                '.' -> {
                    if (next to direction !in visited) {
                        beams.add(next to direction)
                        visited.add(next to direction)
                    }
                }

                '|' -> {
                    if (direction == Point(1, 0) || direction == Point(-1, 0)) {
                        if (next to Point(0, -1) !in visited) {
                            beams.add(next to Point(0, -1))
                            visited.add(next to Point(0, -1))
                        }

                        beams.add(next to Point(0, 1))
                        visited.add(next to Point(0, 1))
                    } else if (next to direction !in visited) {
                        beams.add(next to direction)
                        visited.add(next to direction)
                    }
                }

                '-' -> {
                    if (direction == Point(0, 1) || direction == Point(0, -1)) {
                        if (next to Point(-1, 0) !in visited) {
                            beams.add(next to Point(-1, 0))
                            visited.add(next to Point(-1, 0))
                        }

                        beams.add(next to Point(1, 0))
                        visited.add(next to Point(1, 0))
                    } else if (next to direction !in visited) {
                        beams.add(next to direction)
                        visited.add(next to direction)
                    }
                }

                '/' -> {
                    val newDirection = when (direction) {
                        Point(0, 1) -> Point(-1, 0)
                        Point(0, -1) -> Point(1, 0)
                        Point(1, 0) -> Point(0, -1)
                        else -> Point(0, 1)
                    }

                    if (next to newDirection !in visited) {
                        beams.add(next to newDirection)
                        visited.add(next to newDirection)
                    }
                }

                '\\' -> {
                    val newDirection = when (direction) {
                        Point(0, 1) -> Point(1, 0)
                        Point(0, -1) -> Point(-1, 0)
                        Point(1, 0) -> Point(0, 1)
                        else -> Point(0, -1)
                    }

                    if (next to newDirection !in visited) {
                        beams.add(next to newDirection)
                        visited.add(next to newDirection)
                    }
                }

                else -> throw Exception("Unknown character")
            }
        }
    }

    return active.size
}

fun main() {
    val data = readFile(16)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    return solve(data, Point(-1, 0), Point(1, 0))
}


//Part 2
private fun part2(data: List<String>): Int {
    val height = data.size
    val width = data[0].length

    var max = 0

    repeat(height) { y ->
        repeat(width) { x ->
            if (x == 0) {
                val new = solve(data, Point(x, y), Point(1, 0))
                if (new > max) {
                    max = new
                }
            }

            if (x == width - 1) {
                val new = solve(data, Point(x, y), Point(-1, 0))
                if (new > max) {
                    max = new
                }
            }

            if (y == 0) {
                val new = solve(data, Point(x, y), Point(0, 1))
                if (new > max) {
                    max = new
                }
            }

            if (y == height - 1) {
                val new = solve(data, Point(x, y), Point(0, -1))
                if (new > max) {
                    max = new
                }
            }
        }
    }

    return max
}
