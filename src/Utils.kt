import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 *   Read input file.
 */
fun readFile(day: Int): List<String> = Path("src/Day${day}.txt").readLines()


/**
 *   Point in 2D.
 */
data class Point(val x: Int, val y: Int) {

    fun inBound(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean = x in minX..maxX && y in minY..maxY

    fun neighbors(): List<Point> = NEIGHBORS.map { Point(it.x + this.x, it.y + this.y) }

    companion object {

        val NEIGHBORS: List<Point> = (-1..1)
            .flatMap { x -> (-1..1).map { y -> Point(x, y) } }
            .filterNot { it.x == 0 && it.y == 0 }

    }

}
