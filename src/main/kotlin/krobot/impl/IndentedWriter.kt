/**
 *@author Nikolaus Knop
 */

package krobot.impl

import krobot.ast.Element
import krobot.ast.NoElement
import krobot.ast.RawElement

class IndentedWriter(private val out: Appendable) {
    private var indentation = ""
    private var isNewLine = true

    private fun indent() {
        out.append(indentation)
    }

    fun append(str: String) {
        if (isNewLine) indent()
        out.append(str)
        isNewLine = false
    }

    private fun append(element: Char) {
        if (isNewLine) indent()
        out.append(element)
        isNewLine = false
    }

    fun append(element: Element) {
        element.append(this)
    }

    fun space() {
        append(' ')
    }

    fun appendLine() {
        out.appendLine()
        isNewLine = true
    }

    fun appendLine(str: String) {
        append(str)
        appendLine()
    }

    fun append(element: Any?) {
        when (element) {
            null -> {
            }
            is Char -> append(element)
            is String -> append(element)
            is Element -> append(element)
            else -> append(element.toString())
        }
    }

    fun join(
        elements: Iterable<Any>?,
        separator: Element = NoElement,
        prefix: Element = NoElement,
        postfix: Element = NoElement
    ) {
        if (elements == null || elements.none()) return
        append(prefix)
        append(elements.first())
        for (element in elements.drop(1)) {
            append(separator)
            append(element)
        }
        append(postfix)
    }

    fun join(
        elements: Iterable<Any>?,
        separator: String = ", ",
        prefix: String = "",
        postfix: String = ""
    ) {
        join(elements, RawElement(separator), RawElement(prefix), RawElement(postfix))
    }

    fun join(vararg elements: Any?) {
        if (elements.any { it == null }) return
        for (element in elements) append(element)
    }

    @PublishedApi
    internal fun addIndentation() {
        indentation += "    "
    }

    @PublishedApi
    internal fun dropIndentation() {
        indentation = indentation.drop(4)
    }

    inline fun indented(block: IndentedWriter.() -> Unit) {
        addIndentation()
        block()
        dropIndentation()
    }
}