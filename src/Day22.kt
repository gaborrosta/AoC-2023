private class Brick(val name: String, val start: Point3D, val end: Point3D) {

    constructor(name: String, list: List<Point3D>) : this(name, list.first(), list.last())

    private val length = start.manhattan(end)
    private val orientation: Point3D = if (length != 0) (end - start) / length else Point3D(0, 0, 0)
    val inside = List(length + 1) { start + orientation * it }
    val canFall = (inside.minZ() ?: 0) > 1

    val restsOn = mutableSetOf<Brick>()
    val supports = mutableSetOf<Brick>()

    fun fall(): List<Point3D>? = inside.map { it.zMinus() }.takeIf { it.none { p -> p.z < 1 } }

    fun copy(s: Point3D = start, e: Point3D = end): Brick = Brick(name, s, e)

    fun restOn(other: Brick) = inside.map { it.zMinus() }.takeIf { it.none { p -> p.z < 1 } }?.intersect(other.inside.toSet())?.isNotEmpty() ?: false

    fun surroundings(bricks: List<Brick>) {
        bricks.filter { it != this }.forEach {
            if (it.restOn(this)) {
                it.restsOn.add(this)
                this.supports.add(it)
            } else if (this.restOn(it)) {
                this.restsOn.add(it)
                it.supports.add(this)
            }
        }
    }

}

private fun fall(data: List<Brick>): List<Brick> {
    var bricks = data.map { it.copy() }.toMutableList()
    var remainingDistance = data.maxOf { it.inside.maxZ() ?: 0 }

    while (remainingDistance > 0) {
        val newBricks = arrayListOf<Brick>()

        val iterator = bricks.listIterator()
        while (iterator.hasNext()) {
            val now = iterator.next()
            iterator.remove()

            val new = now.fall()
            if (now.canFall && new != null &&
                bricks.all { it.inside.intersect(new).isEmpty() } && newBricks.all { it.inside.intersect(new).isEmpty() }
            ) {
                newBricks.add(Brick(now.name, new))
            } else {
                newBricks.add(now)
            }
        }

        bricks = newBricks
        remainingDistance--
    }

    bricks.forEach { it.surroundings(bricks) }

    return bricks
}

fun main() {
    val data = fall(readFile(22).mapIndexed { index, line ->
        val (start, end) = line.split("~")
        Brick(index.toString(), Point3D.parse(start), Point3D.parse(end))
    })

    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}


//Part 1
private fun part1(data: List<Brick>): Int {
    return data.count { brick ->
        data.filter { it != brick }.all { (it.restsOn.isNotEmpty() && it.restsOn.minus(brick).isNotEmpty()) || it.restsOn.isEmpty() }
    }
}


//Part 2
private fun part2(data: List<Brick>): Int {
    return data.sumOf { brick ->
        val falling = hashSetOf(brick)
        var newMoving = true

        while (newMoving) {
            newMoving = false
            val newFalling = hashSetOf<Brick>()

            data.filter { it !in falling }.forEach { new ->
                if (new.restsOn.isNotEmpty() && new.restsOn.minus(falling).isEmpty()) {
                    newMoving = true
                    newFalling.add(new)
                }
            }

            falling.addAll(newFalling)
        }

        falling.size - 1
    }
}
