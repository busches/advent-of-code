package utils

// Based off https://gist.github.com/trygvea/a2d9cdbc19ceff3df7eb64ccef3c0597
fun <T> List<T>.permutations(): List<List<T>> = when {
    size > 10 -> throw Exception("You probably dont have enough memory to keep all those permutations")
    size <= 1 -> listOf(this)
    else ->
        drop(1).permutations().map { perm ->
            (indices).map { i ->
                perm.subList(0, i) + first() + perm.drop(i)
            }
        }.flatten()
}

fun <T> List<List<T>>.transpose() = with(this) {
    this@transpose[0].indices.map { i -> map { it[i] } }
}

// https://stackoverflow.com/a/56043547
fun <T> List<T>.combinations(): Sequence<Pair<T, T>> = sequence {
    for (i in 0 until size - 1)
        for (j in i + 1 until size)
            yield(this@combinations[i] to this@combinations[j])
}
