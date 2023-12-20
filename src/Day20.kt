private const val LOW = 0
private const val HIGH = 1

private interface Module {

    val to: List<String>

    fun receive(sender: String, pulse: Int): List<Pair<String, Int>>

}

private class FlipFlop(override val to: List<String>) : Module {

    var on = false

    override fun receive(sender: String, pulse: Int): List<Pair<String, Int>> {
        return if (pulse == LOW) {
            if (on) {
                on = false
                to.map { it to LOW }
            } else {
                on = true
                to.map { it to HIGH }
            }
        } else {
            emptyList()
        }
    }

}

private class Conjunction(override val to: List<String>) : Module {

    val from = mutableMapOf<String, Int>()

    override fun receive(sender: String, pulse: Int): List<Pair<String, Int>> {
        from[sender] = pulse

        return if (from.entries.all { it.value == HIGH }) {
            to.map { it to LOW }
        } else {
            to.map { it to HIGH }
        }
    }

    fun addSender(sender: String) {
        from[sender] = LOW
    }

}

private class Broadcaster(override val to: List<String>) : Module {

    override fun receive(sender: String, pulse: Int): List<Pair<String, Int>> {
        return to.map { it to pulse }
    }

}

private fun readInput(): Map<String, Module> {
    val data = readFile(20).mapNotNull { line ->
        val l = line.split(" -> ")

        if (l[0] == "broadcaster") {
            "broadcaster" to Broadcaster(l[1].split(", "))
        } else if (l[0][0] == '%') {
            l[0].drop(1) to FlipFlop(l[1].split(", "))
        } else if (l[0][0] == '&') {
            l[0].drop(1) to Conjunction(l[1].split(", "))
        } else {
            null
        }
    }.toMap()

    data.forEach { (name, module) ->
        if (module is Conjunction) {
            data.filter { it.value.to.contains(name) }.keys.forEach { module.addSender(it) }
        }
    }

    return data
}

fun main() {
    println("Part 1: ${part1(readInput())}")
    println("Part 2: ${part2()}")
}


//Part 1
private fun part1(data: Map<String, Module>): Int {
    var lows = 0
    var highs = 0

    repeat(1000) {
        lows++
        val pulses = arrayListOf(Triple("button", "broadcaster", LOW))

        while (pulses.isNotEmpty()) {
            val (sender, receiver, pulse) = pulses.removeAt(0)
            //println("$sender, $pulse, $receiver (${data[receiver]}")

            val new = data[receiver]?.receive(sender, pulse) ?: continue
            //println("$new from ${data[receiver]}")
            //println()

            lows += new.count { it.second == LOW }
            highs += new.count { it.second == HIGH }

            pulses.addAll(new.map { (to, what) -> Triple(receiver, to, what) })
        }
        //println("${data["rz"]}, ${data["lf"]}, ${data["br"]}, ${data["fk"]}")
    }

    return lows * highs
}


//Part 2
private fun part2(): Long {
    fun calculateLowPulseSending(to: String, data: Map<String, Module>): Long {
        var i = 1L

        while (true) {
            val pulses = arrayListOf(Triple("button", "broadcaster", LOW))

            while (pulses.isNotEmpty()) {
                val (sender, receiver, pulse) = pulses.removeAt(0)

                if (receiver == to && pulse == LOW) return i

                val new = data[receiver]?.receive(sender, pulse) ?: continue

                pulses.addAll(new.map { (to, what) -> Triple(receiver, to, what) })
            }

            i++
        }
    }

    return lcm(listOf("rz", "lf", "br", "fk").map { calculateLowPulseSending(it, readInput()) })
}
