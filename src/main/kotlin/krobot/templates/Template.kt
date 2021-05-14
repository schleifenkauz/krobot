package krobot.templates

import krobot.impl.CharSource
import krobot.impl.IndentedWriter

public class Template private constructor(private val raw: String, private val parts: List<Part>) {
    override fun toString(): String = raw

    internal sealed interface Part

    internal sealed interface Token: Part

    internal data class Verbatim(val content: String) : Token

    internal data class Interpolate(
        val operator: Char,
        val separator: String,
        val index: Int
    ) : Token

    internal data class Group(val tokens: List<Token>) : Part

    internal fun format(out: IndentedWriter, arguments: List<Any?>) {
        for (part in parts) {
            when (part) {
                is Verbatim -> out.append(part.content)
                is Interpolate -> interpolate(part, out, arguments[part.index])
                is Group -> {
                    if (shouldSkip(part, arguments)) continue
                    for (token in part.tokens) {
                        when (token) {
                            is Verbatim -> out.append(token.content)
                            is Interpolate -> interpolate(token, out, arguments[token.index])
                        }
                    }
                }
            }
        }
    }

    private fun shouldSkip(group: Group, arguments: List<Any?>): Boolean {
        for (token in group.tokens) {
            if (token is Interpolate) {
                val i = token.index
                if (i !in arguments.indices)
                    throw IllegalArgumentException("No argument at index $i")
                val arg = arguments[i] ?: return true
                if (token.operator == '*') {
                    require(arg is Iterable<*>) { "Argument $i must be iterable but is $arg" }
                    if (arg.none() || arg.any { it == null }) return true
                }
            }
        }
        return false
    }

    private fun interpolate(token: Interpolate, out: IndentedWriter, argument: Any?) {
        when (token.operator) {
            '@' -> if (argument != null) out.append(argument)
            '*' -> {
                if (argument == null) return
                val elements = argument as Iterable<Any?>
                if (elements.none() || elements.any { it == null }) return
                out.join(elements.map { it!! }, token.separator, "", "")
            }
        }
    }

    public companion object {
        public fun parse(raw: String): Template {
            val src = CharSource(raw)
            val parts = parseParts(src, inGroup = false)
            return Template(raw, parts)
        }

        private fun parseGroup(src: CharSource): Group {
            val tokens = parseParts(src, inGroup = true).map { it as Token }
            return Group(tokens)
        }

        private fun parseParts(src: CharSource, inGroup: Boolean): List<Part> {
            val dest = mutableListOf<Part>()
            val buf = StringBuilder()
            while (src.hasNext()) {
                when (val c = src.next()) {
                    '@', '*' -> {
                        clearBuffer(buf, dest)
                        var separator = ""
                        if (c == '*') {
                            while (!src.peek().isDigit()) {
                                buf.append(src.next())
                            }
                            separator = buf.toString()
                            buf.clear()
                        }
                        val index = src.next().toString().toIntOrNull() ?: src.error("expected parameter index")
                        dest.add(Interpolate(c, separator, index))
                    }
                    '{' -> {
                        if (inGroup) src.error("nested groups are not allowed")
                        clearBuffer(buf, dest)
                        dest.add(parseGroup(src))
                    }
                    '}' -> {
                        if (inGroup) break
                        src.error("unmatched closing curly bracket")
                    }
                    '\\' -> buf.append(src.next())
                    else -> buf.append(c)
                }
            }
            clearBuffer(buf, dest)
            return dest
        }

        private fun clearBuffer(buf: StringBuilder, dest: MutableList<Part>) {
            if (buf.isNotEmpty()) {
                dest.add(Verbatim(buf.toString()))
                buf.clear()
            }
        }
    }
}