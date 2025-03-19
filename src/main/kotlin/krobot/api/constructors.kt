package krobot.api

import krobot.ast.Constructor
import krobot.ast.Expr

infix fun Constructor.delegate(arguments: List<Expr>): Constructor = apply {
    delegationArguments = arguments
}

fun Constructor.delegate(vararg arguments: Expr): Constructor = delegate(arguments.asList())

inline infix fun Constructor.body(block: BlockRobot.() -> Unit): Constructor = apply {
    body = BlockRobot(imports).apply(block).finish().statements
}