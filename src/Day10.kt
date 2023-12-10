private data class Pipe(val point: Point, val value: Char, val to: Point?)

private fun createPath(data: List<String>): List<Pipe> {
    val startY = data.indexOfFirst { it.contains('S') }
    val startX = data[startY].indexOfFirst { it == 'S' }
    val start = (startX to startY).toPoint()
    val path = arrayListOf(Pipe(start, 'S', null))

    val east = try {
        data[startY][startX + 1]
    } catch (_: Exception) {
        ' '
    }

    val north = try {
        data[startY - 1][startX]
    } catch (_: Exception) {
        ' '
    }

    val south = try {
        data[startY + 1][startX]
    } catch (_: Exception) {
        ' '
    }

    val west = try {
        data[startY][startX - 1]
    } catch (_: Exception) {
        ' '
    }

    if (east in "-J7") {
        when (east) {
            '-' -> path.add(Pipe(((startX + 1) to startY).toPoint(), east, ((startX + 2) to startY).toPoint()))
            'J' -> path.add(Pipe(((startX + 1) to startY).toPoint(), east, ((startX + 1) to (startY - 1)).toPoint()))
            '7' -> path.add(Pipe(((startX + 1) to startY).toPoint(), east, ((startX + 1) to (startY + 1)).toPoint()))
        }
    } else if (north in "|7F") {
        when (north) {
            '|' -> path.add(Pipe((startX to (startY - 1)).toPoint(), north, (startX to (startY - 2)).toPoint()))
            '7' -> path.add(Pipe((startX to (startY - 1)).toPoint(), north, ((startX - 1) to (startY - 1)).toPoint()))
            'F' -> path.add(Pipe((startX to (startY - 1)).toPoint(), north, ((startX + 1) to (startY - 1)).toPoint()))
        }
    } else if (south in "|LJ") {
        when (south) {
            '|' -> path.add(Pipe((startX to (startY + 1)).toPoint(), south, (startX to (startY + 2)).toPoint()))
            'L' -> path.add(Pipe((startX to (startY + 1)).toPoint(), south, ((startX + 1) to (startY + 1)).toPoint()))
            'J' -> path.add(Pipe((startX to (startY + 1)).toPoint(), south, ((startX - 1) to (startY + 1)).toPoint()))
        }
    } else if (west in "-LF") {
        when (west) {
            '-' -> path.add(Pipe(((startX - 1) to startY).toPoint(), west, ((startX - 1) to startY).toPoint()))
            'L' -> path.add(Pipe(((startX - 1) to startY).toPoint(), west, ((startX - 1) to (startY - 1)).toPoint()))
            'F' -> path.add(Pipe(((startX - 1) to startY).toPoint(), west, ((startX - 1) to (startY + 1)).toPoint()))
        }
    }

    while (path.last().to != start) {
        val now = path.last().to!!
        val (nowX, nowY) = now
        val from = path.last().point

        val cameFromWest = from.x + 1 == now.x && from.y == now.y
        val cameFromSouth = from.x == now.x && from.y - 1 == now.y
        val cameFromNorth = from.x == now.x && from.y + 1 == now.y
        val cameFromEast = from.x - 1 == now.x && from.y == now.y

        when (data[now.y][now.x]) {
            '|' -> {
                if (cameFromSouth) {
                    path.add(Pipe(now, '|', (nowX to (nowY - 1)).toPoint()))
                } else if (cameFromNorth) {
                    path.add(Pipe(now, '|', (nowX to (nowY + 1)).toPoint()))
                } else throw Exception("Error with |")
            }

            '-' -> {
                if (cameFromWest) {
                    path.add(Pipe(now, '-', ((nowX + 1) to nowY).toPoint()))
                } else if (cameFromEast) {
                    path.add(Pipe(now, '-', ((nowX - 1) to nowY).toPoint()))
                } else throw Exception("Error with -")
            }

            'L' -> {
                if (cameFromEast) {
                    path.add(Pipe(now, 'L', (nowX to (nowY - 1)).toPoint()))
                } else if (cameFromNorth) {
                    path.add(Pipe(now, 'L', ((nowX + 1) to nowY).toPoint()))
                } else throw Exception("Error with L")
            }

            'J' -> {
                if (cameFromWest) {
                    path.add(Pipe(now, 'J', (nowX to (nowY - 1)).toPoint()))
                } else if (cameFromNorth) {
                    path.add(Pipe(now, 'J', ((nowX - 1) to nowY).toPoint()))
                } else throw Exception("Error with J")
            }

            '7' -> {
                if (cameFromWest) {
                    path.add(Pipe(now, '7', (nowX to (nowY + 1)).toPoint()))
                } else if (cameFromSouth) {
                    path.add(Pipe(now, '7', ((nowX - 1) to nowY).toPoint()))
                } else throw Exception("Error with 7")
            }

            'F' -> {
                if (cameFromSouth) {
                    path.add(Pipe(now, 'F', ((nowX + 1) to nowY).toPoint()))
                } else if (cameFromEast) {
                    path.add(Pipe(now, 'F', (nowX to (nowY + 1)).toPoint()))
                } else throw Exception("Error with F")
            }
        }
    }

    return path
}

fun main() {
    val data = readFile(10)
    val path = createPath(data)

    println("Part 1: ${part1(path)}")
    println("Part 2: ${part2(data, path)}")
}


//Part 1
private fun part1(path: List<Pipe>): Int {
    return path.size / 2
}


//Part 2
private fun part2(data: List<String>, path: List<Pipe>): Int {
    val pathPoints = path.map { it.point }
    var count = 0
    for (y in data.indices) {
        var inside = false
        var foundF = false
        var foundL = false

        for (x in data[y].indices) {
            val p = pathPoints.find { it.x == x && it.y == y }

            if (p == null) {
                if (inside) {
                    count++
                }
            } else {
                //Manually found what S does.
                when (data[y][x]) {
                    '.', '-' -> {}

                    '|', 'S' -> {
                        inside = !inside
                    }

                    'F' -> {
                        foundF = true
                    }

                    'J' -> {
                        if (foundF) {
                            inside = !inside
                            foundF = false
                        } else if (foundL) {
                            foundL = false
                        }
                    }

                    'L' -> {
                        foundL = true
                    }

                    '7' -> {
                        if (foundL) {
                            inside = !inside
                            foundL = false
                        } else if (foundF) {
                            foundF = false
                        }
                    }
                }
            }
        }
    }

    return count
}
