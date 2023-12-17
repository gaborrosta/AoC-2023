import java.util.*

private fun findPath(from: Point, to: Point, data: List<String>, maxStraight: Int, minStraightEnabled: Boolean): Int {
    val width = data[0].length
    val height = data.size

    val frontier = PriorityQueue<Triple<Point, Int, List<Point>>>(compareBy { o ->
        o.third.sumOf { data[it.y][it.x].digitToInt() } + o.first.manhattan(Point(width, height))
    })
    frontier.add(Triple(from, 0, listOf()))

    val visited = hashSetOf<Triple<Point, Point?, Int>>()

    while (frontier.isNotEmpty()) {
        val (me, repeat, path) = frontier.poll()
        val previous = if (path.isNotEmpty()) path.last() else null
        val direction = if (previous != null) me - previous else null

        if (Triple(me, direction, repeat) in visited) {
            continue
        }
        visited.add(Triple(me, direction, repeat))

        if (me == to) {
            return path.drop(1).sumOf { data[it.y][it.x].digitToInt() } + data[me.y][me.x].digitToInt()
        }

        if (repeat < maxStraight && direction != null) {
            val next = me + direction
            if (next.inBound(width - 1, height - 1)) {
                frontier.add(Triple(next, repeat + 1, path + me))
            }
        }

        if ((minStraightEnabled && (repeat >= 4 || direction == null)) || !minStraightEnabled) {
            for (nd in Point.NEIGHBORS_HV) {
                if (nd == direction || nd == (direction?.let { it * -1 })) continue
                val next = me + nd
                if (next.inBound(width - 1, height - 1)) {
                    frontier.add(Triple(next, 1, path + me))
                }
            }
        }
    }

    return -1
}

fun main() {
    val data = readFile(17)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    val width = data[0].length
    val height = data.size

    return findPath(
        from = Point(x = 0, y = 0),
        to = Point(x = width - 1, y = height - 1),
        data = data,
        maxStraight = 3,
        minStraightEnabled = false,
    )
}


//Part 2
private fun part2(data: List<String>): Int {
    val width = data[0].length
    val height = data.size

    return findPath(
        from = Point(x = 0, y = 0),
        to = Point(x = width - 1, y = height - 1),
        data = data,
        maxStraight = 10,
        minStraightEnabled = true,
    )
}
