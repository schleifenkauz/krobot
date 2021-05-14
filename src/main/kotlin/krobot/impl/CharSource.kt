package krobot.impl

internal class CharSource(private val string: String) {
    private var i = -1

    fun hasNext(): Boolean = i + 1 < string.length

    fun peek(): Char = string.getOrElse(i + 1) { error("unexpected end of input") }

    fun next(): Char = string.getOrElse(++i) { error("unexpected end of input") }

    fun error(msg: String): Nothing {
        throw IllegalArgumentException("Error at index $i: $msg")
    }
}