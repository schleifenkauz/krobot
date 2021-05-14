package krobot.api

import krobot.ast.*
import krobot.ast.FunctionBody.Block
import krobot.ast.FunctionBody.SingleExpr

/* ---------------------------------------
Functions
  --------------------------------------- */
public fun Fun.parameters(parameters: List<Parameter>): Fun = apply {
    this.parameters = parameters
}

public fun Fun.parameters(vararg parameters: Parameter): Fun = parameters(parameters.asList())

public infix fun Fun.receiver(type: Type): Fun = apply {
    receiver = type
}

public infix fun Fun.receiver(raw: String): Fun = receiver(type(raw))

public infix fun Fun.returnType(type: Type): Fun = apply {
    returnType = type
}

public infix fun Fun.returnType(raw: String): Fun = returnType(type(raw))

public infix fun Fun.body(block: BlockRobot.() -> Unit): Fun = apply {
    val imports = imports
    body = Block(BlockRobot(imports).apply(block).finish())
}

public infix fun Fun.returns(expr: Expr): Fun = apply {
    body = SingleExpr(expr)
}