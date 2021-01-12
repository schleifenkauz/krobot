/**
 * @author Nikolaus Knop
 */

package krobot.ast

import krobot.api.ImportsCollector
import krobot.ast.IndentedWriter.NewLine

public interface Assignable

public typealias Identifier = String

public sealed class Expr : BlockElement()

internal data class UncheckedExpr(private val str: String) : Expr() {
    override fun append(out: IndentedWriter) {
        out.append(str)
    }
}

internal data class Literal(private val string: String) : Expr() {
    override fun append(out: IndentedWriter) {
        out.append(string)
    }
}

internal data class OperatorApplication(
    private val lhs: Expr,
    private val operator: String,
    private val rhs: Expr
) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append(lhs)
        space()
        append(operator)
        space()
        append(rhs)
    }
}

internal data class ParenthesizedExpr(private val wrapped: Expr) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append('(')
        append(wrapped)
        append(')')
    }
}

internal data class UnaryOperatorApplication(
    private val operand: Expr,
    private val operator: String,
    private val after: Boolean = false
) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        if (after) {
            append(operand)
            append(operator)
        } else {
            append(operand)
            append(operator)
        }
    }
}

internal data class MapAccess(private val map: Expr, private val keys: List<Expr>) : Assignable, Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append(map)
        join(keys, ", ", "[", "]")
    }
}

internal data class InstanceCheck(private val expr: Expr, private val type: Type) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append(expr)
        append(" is ")
        append(type)
    }
}

internal data class TypeCast(private val expr: Expr, private val type: Type, private val safe: Boolean) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append(expr)
        append(" as")
        if (safe) append('?')
        append(" ")
        append(type)
    }
}

internal data class PropertyAccess(
    private val receiver: Expr?,
    private val propertyName: Identifier
) : Expr(), Assignable {
    override fun append(out: IndentedWriter) = with(out) {
        join(receiver, ".")
        append(propertyName)
    }
}

internal data class FunctionCall(
    private val receiver: Expr?,
    private val functionName: Identifier,
    private val typeArguments: List<Type>,
    private val arguments: List<Expr>
) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        join(receiver, '.')
        append(functionName)
        join(typeArguments, prefix = "<", postfix = ">")
        append('(')
        val last = arguments.lastOrNull()
        if (last is Closure) join(arguments.dropLast(1), ", ")
        else join(arguments, ", ")
        append(")")
        if (last is Closure) {
            space()
            append(last)
        }
    }
}

public data class IfExpr internal constructor(
    @PublishedApi internal val imports: ImportsCollector,
    private val condition: Expr,
    @PublishedApi internal var then: Body?,
    @PublishedApi internal var `else`: Body?
) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append("if(")
        append(condition)
        append(") ")
        append(then!!)
        if (then!!.statements.size == 1) appendLine()
        else space()
        join("else ", `else`)
    }
}

@PublishedApi internal data class WhenEntry(private val condition: Expr?, private val body: Body) : Element() {
    override fun append(out: IndentedWriter) = with(out) {
        if (condition != null) append(condition)
        else append("else")
        append(" -> ")
        append(body)
    }
}

internal data class WhenExpr(private val entries: List<WhenEntry>) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append("when ")
        block(entries)
    }
}

internal sealed class SubjectWhenCondition : Element() {
    data class Equals(private val value: Expr) : SubjectWhenCondition() {
        override fun append(out: IndentedWriter) = value.append(out)
    }

    data class InstanceOf(private val type: Type) : SubjectWhenCondition() {
        override fun append(out: IndentedWriter) = with(out) {
            append("is ")
            append(type)
        }
    }

    data class ContainedIn(private val collection: Expr) : SubjectWhenCondition() {
        override fun append(out: IndentedWriter) = with(out) {
            append("in ")
            append(collection)
        }
    }
}

@PublishedApi internal data class SubjectWhenEntry(
    private val conditions: List<SubjectWhenCondition>?,
    private val body: Body
) : Element() {
    override fun append(out: IndentedWriter) = with(out) {
        if (conditions != null) join(conditions, ", ")
        else append("else")
        append(" -> ")
        append(body)
    }
}

internal data class SubjectWhenExpr(private val subject: Expr, private val entries: List<SubjectWhenEntry>) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append("when(")
        append(subject)
        append(") ")
        block(entries)
    }
}

@PublishedApi internal data class Closure(
    private val parameters: List<Parameter>,
    private val body: List<BlockElement>
) : Expr() {
    override fun append(out: IndentedWriter) = with(out) {
        append("{ ")
        join(parameters, ", ")
        if (parameters.isNotEmpty()) append(" -> ")
        when (body.size) {
            0    -> {
            }
            1    -> {
                append(body.single())
                space()
            }
            else -> {
                appendLine()
                indented {
                    join(body, NewLine)
                }
                appendLine()
            }
        }
        append("}")
    }
}

internal object DummyExpr : Expr() {
    override fun append(out: IndentedWriter) {
        throw AssertionError("Cannot append dummy expr")
    }
}