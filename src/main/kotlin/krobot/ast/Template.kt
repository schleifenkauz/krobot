package krobot.ast

import krobot.impl.CharSource
import krobot.impl.IndentedWriter

public class Template private constructor(private val raw: String, private val parts: List<Part>) {
    override fun toString(): String = raw

    internal sealed interface Part

    internal sealed interface Token

    internal data class Verbatim(val content: String) : Part, Token

    internal data class Group(val tokens: List<Token>) : Part

    internal data class Interpolate(
        val operator: Char,
        val separator: String,
        val index: Int,
        val questionMark: Boolean
    ) : Token

    internal fun format(out: IndentedWriter, arguments: List<Any?>) {
        for (part in parts) {
            when (part) {
                is Verbatim -> out.append(part.content)
                is Group -> {
                    var skip = false
                    for (token in part.tokens) {
                        if (token is Interpolate) {
                            val i = token.index
                            if (i !in arguments.indices)
                                throw IllegalArgumentException("No argument at index $i")
                            val arg = arguments[i]
                            if (token.questionMark && arg == null) skip = true
                            if (token.operator == '*') {
                                require(arg is Iterable<*>) { "Argument $i must be iterable but is $arg" }
                                if (token.questionMark && arg.none()) skip = true
                                if (arg.any { it == null }) {
                                    require(token.questionMark) { "Iterable contains null-elements and is not marked with question mark" }
                                    skip = true
                                }
                            }
                            if (skip) break
                        }
                    }
                    if (skip) continue
                    for (token in part.tokens) {
                        when (token) {
                            is Verbatim -> out.append(token.content)
                            is Interpolate -> {
                                when (token.operator) {
                                    '@' -> out.append(arguments[token.index])
                                    '*' -> {
                                        @Suppress("UNCHECKED_CAST")
                                        val elements = arguments[token.index] as Iterable<Any>
                                        out.join(elements, token.separator, "", "")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public companion object {
        public fun parse(raw: String): Template {
            val src = CharSource(raw)
            val parts = mutableListOf<Part>()
            val buf = StringBuilder()
            while (src.hasNext()) {
                when (val c = src.next()) {
                    '{' -> {
                        if (buf.isNotEmpty()) {
                            parts.add(Verbatim(buf.toString()))
                            buf.clear()
                        }
                        val tokens = mutableListOf<Token>()
                        while (src.hasNext()) {
                            when (val d = src.next()) {
                                '@', '*' -> {
                                    if (buf.isNotEmpty()) {
                                        tokens.add(Verbatim(buf.toString()))
                                        buf.clear()
                                    }
                                    var separator = ""
                                    if (d == '*') {
                                        while (!src.peek().isDigit()) {
                                            buf.append(src.next())
                                        }
                                        separator = buf.toString()
                                        buf.clear()
                                    }
                                    val variable = src.next().toString().toInt()
                                    var questionMark = false
                                    if (src.peek() == '?') {
                                        questionMark = true
                                        src.next()
                                    }
                                    tokens.add(Interpolate(d, separator, variable, questionMark))
                                }
                                '}' -> {
                                    if (buf.isNotEmpty()) {
                                        tokens.add(Verbatim(buf.toString()))
                                        buf.clear()
                                    }
                                    parts.add(Group(tokens))
                                    break
                                }
                                '{' -> src.error("illegal opening bracket inside group")
                                '\\' -> buf.append(src.next())
                                else -> buf.append(d)
                            }
                        }
                    }
                    '}' -> src.error("unmatched closing bracket")
                    '\\' -> buf.append(src.next())
                    else -> buf.append(c)
                }
            }
            return Template(raw, parts)
        }
    }
}