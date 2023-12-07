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


/**
 *   Create combination list with repetitions.
 *
 *   I found this code several years ago, it is not mine.
 */
fun <T> combinationWithRepetition(arr: Array<T>, r: Int): List<Map<T, Number>> {
    val result = arrayListOf<ArrayList<T>>()

    /**
     * @param chosen[] Temporary array to store indices of current combination
     * @param arr[] Input Array
     * @param start Starting indexes in arr[]
     * @param end Ending indexes in arr[]
     * @param r Size of a combination to be printed
     */
    fun combinationCalcRecursive(chosen: IntArray, arr: Array<T>, index: Int, r: Int, start: Int, end: Int) {
        if (index == r) {
            val n = arrayListOf<T>()
            for (i in 0..<r) n.add(arr[chosen[i]])
            result.add(n)
            return
        }

        for (i in start..end) {
            chosen[index] = i
            combinationCalcRecursive(chosen, arr, index + 1, r, i, end)
        }
    }

    combinationCalcRecursive(IntArray(r + 1), arr, 0, r, 0, arr.size - 1)
    return result.map { combination -> combination.associateWith { n -> combination.count { it == n } } }
}
