package krobot.api

import krobot.ast.Constructor
import krobot.ast.Expr

public infix fun Constructor.delegate(arguments: List<Expr>): Constructor = apply {
    delegationArguments = arguments
}

public fun Constructor.delegate(vararg arguments: Expr): Constructor = delegate(arguments.asList())

public inline infix fun Constructor.body(block: BlockRobot.() -> Unit): Constructor = apply {
    body = BlockRobot(imports).apply(block).finish().statements
}