import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph

fun main() {
    val data = readFile(25)

    println("Part 1: ${part1(data)}")
}


//Part 1
private fun part1(data: List<String>): Int {
    val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

    data.forEach { line ->
        val (name, others) = line.split(": ")
        graph.addVertex(name)

        others.split(" ").forEach { other ->
            graph.addVertex(other)
            graph.addEdge(name, other)
        }
    }

    val oneSide = StoerWagnerMinimumCut(graph).minCut()

    return (graph.vertexSet().size - oneSide.size) * oneSide.size
}
