import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 *   Read input file.
 */
fun readFile(day: Int): List<String> = Path("src/Day${day}.txt").readLines()
