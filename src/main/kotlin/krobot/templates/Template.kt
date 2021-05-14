package krobot.templates

import krobot.impl.CharSource
import krobot.impl.IndentedWriter

public class Template private constructor(private val raw: String, private val parts: List<Part>) {
    override fun toString(): String = raw

    internal sealed interface Part

    internal data class Verbatim(val content: String) : Part

    internal data class Transformation(val index: Int, val body: Part)

    internal data class Interpolate(
        val operator: Char,
        val separator: Part?,
        val index: Int,
        val transformation: Transformation?,
    ) : Part

    internal data class Group(val parts: List<Part>) : Part

    internal fun format(out: IndentedWriter, arguments: List<Any?>) {
        for (part in parts) out.format(part, arguments, emptyMap())
    }

    private fun IndentedWriter.format(part: Part, arguments: List<Any?>, scope: Map<Int, Any>) {
        when (part) {
            is Verbatim -> append(part.content)
            is Interpolate -> interpolate(part, arguments, scope)
            is Group -> {
                if (shouldSkip(part, arguments, scope)) return
                for (p in part.parts) format(p, arguments, scope)
            }
        }
    }

    private fun shouldSkip(group: Group, arguments: List<Any?>, scope: Map<Int, Any>): Boolean {
        for (part in group.parts) {
            when (part) {
                is Interpolate -> {
                    val i = part.index
                    if (i - 1 !in arguments.indices && i !in scope)
                        throw IllegalArgumentException("No argument at index $i")
                    val arg = scope[i] ?: arguments[i - 1] ?: return true
                    if (part.operator == '*') {
                        require(arg is Iterable<*>) { "Argument $i must be iterable but is $arg" }
                        if (arg.none() || arg.any { it == null }) return true
                    }
                }
                is Group -> {
                    if (shouldSkip(part, arguments, scope)) return true
                }
                is Verbatim -> continue
            }
        }
        return false
    }

    private fun IndentedWriter.interpolate(token: Interpolate, arguments: List<Any?>, scope: Map<Int, Any>) {
        val argument = scope[token.index] ?: arguments[token.index - 1]
        when (token.operator) {
            '@' -> if (argument != null) append(argument)
            '*' -> {
                if (argument == null) return
                val elements = argument as Iterable<Any?>
                if (elements.none() || elements.any { it == null }) return
                val itr = elements.iterator()
                while (true) {
                    val e = itr.next()
                    if (token.transformation != null) {
                        val (index, body) = token.transformation
                        format(body, arguments, scope + (index to e!!))
                    } else append(e)
                    if (!itr.hasNext()) break
                    format(token.separator!!, arguments, scope)
                }
            }
        }
    }

    public companion object {
        public fun parse(raw: String): Template {
            val src = CharSource(raw)
            val parts = parseParts(src, braceNesting = 0, bracketNesting = 0)
            return Template(raw, parts)
        }

        private fun parseParts(src: CharSource, braceNesting: Int, bracketNesting: Int): List<Part> {
            val dest = mutableListOf<Part>()
            val buf = StringBuilder()
            while (src.hasNext()) {
                when (val c = src.next()) {
                    '@', '*' -> {
                        clearBuffer(buf, dest)
                        val index = readIndex(src)
                        val separator = if (c == '*') readSeparator(src) else null
                        val transformation = if (src.hasNext() && src.peek() == '[') {
                            src.next()
                            val i = readIndex(src)
                            if (src.next() != '.') src.error("expected dot")
                            val parts = parseParts(src, 0, bracketNesting + 1)
                            val body = if (parts.size == 1) parts[0] else Group(parts)
                            Transformation(i, body)
                        } else null
                        dest.add(Interpolate(c, separator, index, transformation))
                    }
                    '[' -> src.error("illegal bracket")
                    ']' -> {
                        if (bracketNesting < 1) src.error("unmatched closing curly bracket")
                        if (braceNesting >= 1) src.error("unclosed brace")
                        break
                    }
                    '{' -> {
                        clearBuffer(buf, dest)
                        val tokens = parseParts(src, braceNesting + 1, bracketNesting)
                        dest.add(Group(tokens))
                    }
                    '}' -> {
                        if (braceNesting >= 1) break
                        src.error("unmatched closing curly bracket")
                    }
                    '\\' -> buf.append(src.next())
                    else -> buf.append(c)
                }
            }
            clearBuffer(buf, dest)
            return dest
        }

        private fun readIndex(src: CharSource): Int {
            var index = src.next().toString().toIntOrNull() ?: src.error("expected parameter index")
            while (src.hasNext() && src.peek() in '0'..'9') {
                index = index * 10 + (src.next() - '0')
            }
            return index
        }

        private fun readSeparator(src: CharSource): Part {
            if (src.peek() != '{') return Verbatim(src.next().toString())
            src.next()
            return Group(parseParts(src, braceNesting = 1, bracketNesting = 0))
        }

        private fun clearBuffer(buf: StringBuilder, dest: MutableList<Part>) {
            if (buf.isNotEmpty()) {
                dest.add(Verbatim(buf.toString()))
                buf.clear()
            }
        }
    }
}