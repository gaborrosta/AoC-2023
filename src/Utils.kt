import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

/**
 *   Read input file.
 */
fun readFile(day: Int): List<String> = Path("src/Day${day}.txt").readLines()


/**
 *   Point in 2D.
 */
data class Point(val x: Int, val y: Int) {

    fun inBound(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean = x in minX..maxX && y in minY..maxY
    fun inBound(maxX: Int, maxY: Int): Boolean = inBound(0, maxX, 0, maxY)

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)

    operator fun minus(other: Point): Point = Point(x - other.x, y - other.y)

    operator fun times(amount: Int): Point = Point(x * amount, y * amount)

    fun manhattan(other: Point): Int = abs(x - other.x) + abs(y - other.y)

    fun neighbors(): List<Point> = NEIGHBORS.map { Point(it.x + this.x, it.y + this.y) }

    companion object {

        val NEIGHBORS: List<Point> = (-1..1)
            .flatMap { x -> (-1..1).map { y -> Point(x, y) } }
            .filterNot { it.x == 0 && it.y == 0 }

        val NEIGHBORS_H: List<Point> = listOf(Point(-1, 0), Point(1, 0))
        val NEIGHBORS_V: List<Point> = listOf(Point(0, -1), Point(0, 1))
        val NEIGHBORS_HV: List<Point> = NEIGHBORS_H + NEIGHBORS_V

    }

}

fun Pair<Int, Int>.toPoint(): Point = Point(this.first, this.second)


/**
 *   Create combination list with repetitions.
 *
 *   I found this code several years ago, it is not mine.
 */
fun <T> combinations(seed: Iterable<T>, count: Int): List<List<T>> {

    fun inner(acc: List<List<T>>, remaining: Int): List<List<T>> = when (remaining) {
        0 -> acc
        count -> inner(seed.map { s -> listOf(s) }, remaining - 1)
        else -> inner(seed.flatMap { s -> acc.map { list -> list + s } }, remaining - 1)
    }

    return inner(emptyList(), count)
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


/**
 *   Greatest common divisor.
 */
fun gcd(a: Long, b: Long): Long {
    var aNumber = a
    var bNumber = b

    while (bNumber > 0) {
        val temp = bNumber
        bNumber = aNumber % bNumber
        aNumber = temp
    }

    return aNumber
}

/**
 *   Greatest common divisor.
 */
fun gcd(input: List<Long>): Long {
    var result = input[0]
    for (i in 1..<input.size) result = gcd(result, input[i])
    return result
}

/**
 *   Lowest common multiple.
 */
fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

/**
 *   Lowest common multiple.
 */
fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1..<input.size) result = lcm(result, input[i])
    return result
}


/**
 *   Rotates a list of strings, i.e. a matrix of chars stored as strings of
 *   rows.
 */
fun Collection<String>.rotateStrings(): List<String> {
    return this.flatMap { it.withIndex() }.groupBy({ (i, _) -> i }, { (_, v) -> v }).map { (_, v) -> v.reversed().joinToString("") }
}

/**
 *   Rotates a matrix.
 */
fun <T> Collection<Collection<T>>.rotate(): List<List<T>> {
    return this.flatMap { it.withIndex() }.groupBy({ (i, _) -> i }, { (_, v) -> v }).map { (_, v) -> v.reversed() }
}


/**
 *   Returns the indices of the elements matching the given [predicate]. The
 *   returned list is empty if there are no such elements.
 */
inline fun <T> Iterable<T>.indicesOf(predicate: (T) -> Boolean): List<Int> {
    val indices = arrayListOf<Int>()
    for ((index, item) in this.withIndex()) {
        if (predicate(item)) {
            indices.add(index)
        }
    }

    return indices
}


/**
 *   Groups the consecutive elements of the collection by the given
 *   [shouldCreateNewGroup] function.
 *
 *
 *   @param keepFirstElement Whether the first element should be added to the
 *   group.
 *
 *   @param shouldCreateNewGroup The function that determines whether a new
 *   group should be created.
 */
fun <T> Iterable<T>.groupConsecutiveBy(keepFirstElement: Boolean = true, shouldCreateNewGroup: (Int, T) -> Boolean): List<List<T>> =
    if (!this.any()) {
        emptyList()
    } else {
        val start = mutableListOf(mutableListOf(this.first()))
        this.drop(1).foldIndexed(start) { index, groups, value ->
            if (shouldCreateNewGroup(index + 1, value)) {
                if (keepFirstElement) {
                    groups.add(mutableListOf(value))
                } else {
                    groups.add(mutableListOf())
                }
            } else {
                groups.last().add(value)
            }

            groups
        }
    }

/**
 *   Recognises repetition loop in a list and returns the value at the desired
 *   index.
 *
 *   I found this code several years ago, it is not mine.
 */
fun recognisePatternInList(history: List<Long>, repetition: Long): Long {
    val diffHistory = history.zipWithNext().map { (a, b) -> b - a }

    val loopStart = 200
    val markerLength = 10
    val marker = diffHistory.subList(loopStart, loopStart + markerLength)

    var loopHeight = -1L
    var loopLength = -1
    val heightBeforeLoop = history[loopStart - 1]
    for (i in loopStart + markerLength..<diffHistory.size) {
        if (marker == diffHistory.subList(i, i + markerLength)) {
            loopLength = i - loopStart
            loopHeight = history[i - 1] - heightBeforeLoop
            break
        }
    }

    val numFullLoops = (repetition - loopStart) / loopLength
    val offsetIntoLastLoop = ((repetition - loopStart) % loopLength).toInt()
    val extraHeight = history[loopStart + offsetIntoLastLoop] - heightBeforeLoop
    return heightBeforeLoop + loopHeight * numFullLoops + extraHeight
}
