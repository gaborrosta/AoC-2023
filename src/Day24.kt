private class Point3DDouble(val x: Double, val y: Double, val z: Double) {

    companion object {
        fun parse(v: String): Point3DDouble = Point3D.DEFAULT_PARSE_REGEX.matchEntire(v)?.groupValues?.drop(1)
            ?.let { (x, y, z) -> Point3DDouble(x.toDouble(), y.toDouble(), z.toDouble()) } ?: throw Exception("Cannot parse.")
    }

}

private class Hailstone(val zero: Point3DDouble, val velocity: Point3DDouble) {

    fun intersect2D(other: Hailstone): Pair<Double, Double>? {
        val denominator = (other.velocity.y - (other.velocity.x * this.velocity.y) / (this.velocity.x))

        if (denominator == 0.0) {
            return null
        }

        val b =
            (this.zero.y - other.zero.y + (other.zero.x * this.velocity.y) / (this.velocity.x) - (this.zero.x * this.velocity.y) / (this.velocity.x)) / denominator
        val a = (other.zero.x + b * other.velocity.x - this.zero.x) / (this.velocity.x)

        if (a < 0.0 || b < 0.0) {
            return null
        }

        val x = this.zero.x + a * this.velocity.x
        val y = this.zero.y + a * this.velocity.y

        return Pair(x, y)
    }

}

fun main() {
    val data = readFile(24).map { line ->
        val (start, end) = line.split("@")
        Hailstone(Point3DDouble.parse(start), Point3DDouble.parse(end))
    }

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2()}")
}


//Part 1
private fun part1(data: List<Hailstone>): Int {
    return data.sumOf { me ->
        data.count { other ->
            if (me != other) {
                val intersect = me.intersect2D(other)

                if (intersect != null) {
                    intersect.first in 200000000000000.0..400000000000000.0 && intersect.second in 200000000000000.00..400000000000000.0
                } else {
                    false
                }
            } else {
                false
            }
        }
    } / 2
}


//Part 2
private fun part2(): Int {
    //I did not manage to solve this part on my own, I needed help from others' solutions.
    return -1
}
