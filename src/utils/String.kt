fun String.extractInts(): Sequence<Long> = "(\\d+)".toRegex().findAll(this)
    .map { it.value }
    .map { it.toLong() }
