import java.awt.geom.Area
import java.awt.geom.GeneralPath

fun main() {
    val data = readFile(18).map {
        val (dir, amount, col) = it.split(" ")

        val direction = when (dir) {
            "R" -> Point(1, 0)
            "L" -> Point(-1, 0)
            "D" -> Point(0, 1)
            "U" -> Point(0, -1)
            else -> throw Exception("Unknown direction")
        }

        val color = col.removeSurrounding("(#", ")")

        Triple(direction, amount.toInt(), color)
    }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<Triple<Point, Int, String>>): Int {
    var now = Point(0, 0)
    val trench = arrayListOf<Point>()
    val interior = arrayListOf<Point>()

    val polygon = GeneralPath(GeneralPath.WIND_EVEN_ODD, data.size)
    polygon.moveTo(0.0, 0.0)

    data.forEach { (direction, amount, _) ->
        polygon.lineTo((now + direction * amount).x.toFloat(), (now + direction * amount).y.toFloat())
        repeat(amount) {
            now += direction
            trench.add(now)
        }
    }

    val area = Area(polygon)

    val minX = trench.minX() ?: 0
    val minY = trench.minY() ?: 0
    val maxX = trench.maxX() ?: 0
    val maxY = trench.maxY() ?: 0

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (area.contains(x.toDouble(), y.toDouble())) {
                interior.add(Point(x, y))
            }
        }
    }

    return (interior + trench).distinct().size
}


//Part 2
private fun part2(data: List<Triple<Point, Int, String>>): Long {
    val data2 = data.map { (_, _, color) ->
        val distance = color.take(5).toInt(16)

        val direction = when (color.last()) {
            '0' -> Point(1, 0)
            '2' -> Point(-1, 0)
            '1' -> Point(0, 1)
            '3' -> Point(0, -1)
            else -> throw Exception("Unknown value")
        }

        direction to distance
    }

    val vertices = data2.runningFold(Point(0, 0)) { last, line -> last + line.first * line.second }

    val first = vertices.first()
    val last = vertices.last()
    val area = vertices.windowed(2, 1).fold(0L) { acc, value ->
        val (from, to) = value

        acc + (from.x * to.y.toLong() - to.x * from.y.toLong())
    }

    return (area + last.x * first.y - first.x * last.y) / 2 + data2.sumOf { it.second } / 2 + 1
}
