package krobot.api

import krobot.ast.*
import krobot.ast.FunctionBody.Block
import krobot.ast.FunctionBody.SingleExpr

/* ---------------------------------------
Functions
  --------------------------------------- */
fun Fun.parameters(parameters: List<Parameter>): Fun = apply {
    this.parameters = parameters
}

fun Fun.parameters(vararg parameters: Parameter): Fun = parameters(parameters.asList())

infix fun Fun.receiver(type: Type): Fun = apply {
    receiver = type
}

infix fun Fun.receiver(raw: String): Fun = receiver(type(raw))

infix fun Fun.returnType(type: Type): Fun = apply {
    returnType = type
}

infix fun Fun.returnType(raw: String): Fun = returnType(type(raw))

infix fun Fun.body(block: BlockRobot.() -> Unit): Fun = apply {
    val imports = imports
    body = Block(BlockRobot(imports).apply(block).finish())
}

infix fun Fun.returns(expr: Expr): Fun = apply {
    body = SingleExpr(expr)
}