package krobot.ast

import krobot.impl.IndentedWriter
import krobot.templates.Template

object NoElement : Element {
    override fun append(out: IndentedWriter) {}
}

object NewLine : Element {
    override fun append(out: IndentedWriter) {
        out.appendLine()
    }
}

object Space : Element {
    override fun append(out: IndentedWriter) {
        out.space()
    }
}

internal data class SingleLineComment(private val content: String) : Declaration {
    override fun append(out: IndentedWriter) {
        out.append("//")
        out.append(content)
    }
}

data class MultiLineComment(
    private val lines: List<String>,
    private val isKDoc: Boolean
) : Declaration {
    override fun append(out: IndentedWriter): Unit = with(out) {
        if (isKDoc) appendLine("/**")
        else appendLine("/*")
        for (line in lines) {
            append("* ")
            appendLine(line)
        }
        append("*/")
    }
}

interface UniversalElement : Declaration, Statement, Expr, Type

internal data class RawElement(private val str: String) : UniversalElement {
    override fun append(out: IndentedWriter) {
        out.append(str)
    }
}

internal data class UserDefinedElement(private val block: IndentedWriter.() -> Unit) : UniversalElement {
    override fun append(out: IndentedWriter) {
        out.block()
    }
}

internal data class TemplateElement(
    private val template: Template,
    private val arguments: List<Any?>
) : UniversalElement {
    override fun append(out: IndentedWriter) {
        template.format(out, arguments)
    }
}