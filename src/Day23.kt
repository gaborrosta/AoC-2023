private class Step(val point: Point, val slope: Point? = null)

fun main() {
    val data = readFile(23).withIndex().flatMap { (y, line) ->
        line.withIndex().mapNotNull { (x, ch) ->
            when (ch) {
                '.' -> Step(Point(x, y))
                '#' -> null
                '>' -> Step(Point(x, y), Point(1, 0))
                '^' -> Step(Point(x, y), Point(0, -1))
                'v' -> Step(Point(x, y), Point(0, 1))
                '<' -> Step(Point(x, y), Point(-1, 0))
                else -> throw Exception("Unknown character")
            }
        }
    }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<Step>): Int {
    val end = data.last().point

    val frontier = ArrayList<Pair<Step, List<Point>>>()
    frontier.add(Pair(data.first(), listOf()))

    val results = arrayListOf<Int>()

    while (frontier.isNotEmpty()) {
        val (me, path) = frontier.last()
        frontier.removeAt(frontier.size - 1)

        if (me.point in path) {
            continue
        }

        if (me.point == end) {
            results.add(path.size)
            continue
        }

        if (me.slope != null) {
            val next = me.point + me.slope
            data.find { it.point == next }?.let { frontier.add(Pair(it, path + me.point)) }
        } else {
            for (nd in Point.NEIGHBORS_HV) {
                val next = me.point + nd
                data.filter { it.point == next }.forEach { frontier.add(Pair(it, path + me.point)) }
            }
        }
    }

    return results.max()
}


//Part 2
private fun part2(data: List<Step>): Int {
    val end = data.last().point

    val adjacencies = data.associateTo(HashMap()) { step ->
        step.point to data.filter { it.point in step.point.neighborsHv() }.map { it.point }.associateWithTo(HashMap()) { 1 }
    }

    data.forEach { step ->
        adjacencies[step.point]?.takeIf { it.size == 2 }?.let { neighbours ->
            val (left, right) = neighbours.keys.toList()
            val totalSteps = neighbours[left]!! + neighbours[right]!!

            adjacencies.getOrPut(left) { hashMapOf() }.merge(right, totalSteps, ::maxOf)
            adjacencies.getOrPut(right) { hashMapOf() }.merge(left, totalSteps, ::maxOf)

            adjacencies[left]?.remove(step.point)
            adjacencies[right]?.remove(step.point)

            adjacencies.remove(step.point)
        }
    }

    val frontier = ArrayList<Triple<Point, Int, List<Point>>>()
    frontier.add(Triple(data.first().point, 0, emptyList()))

    val results = arrayListOf<Int>()

    while (frontier.isNotEmpty()) {
        val (me, cost, path) = frontier.last()
        frontier.removeAt(frontier.size - 1)

        if (me in path) {
            continue
        }

        if (me == end) {
            results.add(cost)
            continue
        }

        for (next in (adjacencies[me] ?: emptyMap())) {
            frontier.add(Triple(next.key, cost + next.value, path + me))
        }
    }

    return results.max()
}
