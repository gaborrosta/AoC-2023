import java.util.*

fun main() {
    val data = readFile(21)

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2()}")
}


//Part 1
private fun part1(data: List<String>): Int {
    val startY = data.indexOfFirst { it.contains("S") }
    val startX = data[startY].indexOf("S")
    val start = Point(startX, startY)

    val frontier = PriorityQueue<Pair<Point, Int>>(compareBy { it.second })
    frontier.add(start to 0)

    val visited = hashSetOf<Pair<Point, Int>>()

    val width = data[0].length
    val height = data.size

    while (frontier.isNotEmpty()) {
        val (point, steps) = frontier.poll()

        if ((point to steps) in visited || steps > 64) {
            continue
        }
        visited.add(point to steps)

        for (nd in Point.NEIGHBORS_HV) {
            val next = point + nd
            if (next.inBound(width - 1, height - 1) && data[next.y][next.x] != '#') {
                frontier.add(next to (steps + 1))
            }
        }
    }

    return visited.count { it.second == 64 }
}


//Part 2
private fun part2(): Int {
    //I did not manage to solve this part on my own, I needed help from others' solutions.
    return -1
}
