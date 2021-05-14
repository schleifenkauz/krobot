/**
 * @author Nikolaus Knop
 */

package krobot.ast

import krobot.impl.IndentedWriter

public sealed interface Statement : BlockElement

internal data class Assignment(private val assigned: Assignable, private val value: Expr) : Statement {
    override fun append(out: IndentedWriter) = with(out) {
        append(assigned)
        append(" = ")
        append(value)
    }
}

internal data class AugmentedAssignment(
    private val assigned: Assignable,
    private val operator: String,
    private val value: Expr
) : Statement {
    override fun append(out: IndentedWriter) = with(out) {
        append(assigned)
        space()
        append(operator)
        append("= ")
        append(value)
    }
}

@PublishedApi internal data class Body(val statements: List<BlockElement>) : Element {
    override fun append(out: IndentedWriter) {
        if (statements.size == 1) statements.single().append(out)
        else out.block(statements)
    }
}

@PublishedApi internal data class ForLoop(
    private val name: Identifier,
    private val type: Type?,
    private val iterable: Expr,
    private val body: Body
) : Statement {
    override fun append(out: IndentedWriter) = with(out) {
        append("for(")
        append(name)
        join(": ", type)
        append(" in ")
        append(iterable)
        append(") ")
        append(body)
    }
}

@PublishedApi internal data class WhileLoop(private val condition: Expr, private val body: Body) : Statement {
    override fun append(out: IndentedWriter) = with(out) {
        append("while(")
        append(condition)
        append(") ")
        append(body)
    }
}