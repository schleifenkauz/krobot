/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public fun lit(value: Boolean): Expr = Literal("$value")
public fun lit(value: Byte): Expr = Literal("${value}.toByte()")
public fun lit(value: Short): Expr = Literal("${value}.toShort()")
public fun lit(value: Char): Expr = Literal("'$value'")
public fun lit(value: Int): Expr = Literal("$value")
public fun lit(value: Long): Expr = Literal("${value}l")
public fun lit(value: String): Expr = Literal("\"$value\"")
public fun lit(value: Double): Expr = Literal("$value")
public fun lit(value: Float): Expr = Literal("${value}f")
public val `null`: Expr get() = get("null")
public val `this`: Expr get() = get("this")
public fun `this`(scope: String): Expr = get("this@$scope")
public val `super`: Expr get() = get("super")

public val Any?.e: Expr get() = UncheckedExpr(toString())

public infix fun Expr.select(name: Identifier): Expr = PropertyAccess(this, name)

public fun get(name: Identifier): Expr = PropertyAccess(null, name)

public fun call(name: Identifier, typeArguments: List<Type>, arguments: List<Expr>): Expr =
    FunctionCall(null, name, typeArguments, arguments)

public fun call(name: Identifier, typeArguments: List<Type>, vararg arguments: Expr): Expr =
    call(name, typeArguments, arguments.asList())

public fun call(name: Identifier, arguments: List<Expr>): Expr =
    call(name, emptyList(), arguments)

public fun call(name: Identifier, vararg arguments: Expr): Expr =
    call(name, arguments.asList())

public operator fun String.invoke(
    typeArguments: List<Type> = emptyList(),
    arguments: List<Expr> = emptyList()
): Expr = FunctionCall(null, this, typeArguments, arguments)

public operator fun String.invoke(arguments: List<Expr>): Expr = invoke(emptyList(), arguments)

public operator fun String.invoke(vararg arguments: Expr): Expr = invoke(arguments.asList())

public fun Expr.call(
    name: Identifier,
    typeArguments: List<Type>,
    arguments: List<Expr>
): Expr = FunctionCall(this, name, typeArguments, arguments)

public fun Expr.call(
    name: Identifier,
    typeArguments: List<Type>,
    vararg arguments: Expr
): Expr = call(name, typeArguments, arguments.asList())

public fun Expr.call(
    name: Identifier,
    arguments: List<Expr>
): Expr = this.call(name, emptyList(), arguments)

public fun Expr.call(name: Identifier, vararg arguments: Expr): Expr =
    this.call(name, arguments.asList())

public infix fun Expr.call(name: Identifier): Expr = call(name, emptyList<Type>())

public inline fun KotlinRobot.closure(parameters: List<Parameter>, block: BlockRobot.() -> Unit): Expr {
    val body = makeBody(imports, block)
    return Closure(parameters, body.statements)
}

@JvmName("closure2") public inline fun KotlinRobot.closure(
    parameters: List<String>,
    block: BlockRobot.() -> Unit
): Expr = closure(parameters.map { Parameter(it) }, block)

public inline fun KotlinRobot.closure(vararg parameters: String, block: BlockRobot.() -> Unit): Expr =
    closure(parameters.asList(), block)

public inline fun KotlinRobot.closure(vararg parameters: Parameter, block: BlockRobot.() -> Unit): Expr =
    closure(parameters.asList(), block)

public inline fun KotlinRobot.closure(block: BlockRobot.() -> Unit): Expr = closure(emptyList<String>(), block)

public operator fun Expr.plus(other: Expr): Expr = OperatorApplication(this, "+", other)
public operator fun Expr.minus(other: Expr): Expr = OperatorApplication(this, "-", other)
public operator fun Expr.times(other: Expr): Expr = OperatorApplication(this, "*", other)
public operator fun Expr.div(other: Expr): Expr = OperatorApplication(this, "/", other)
public operator fun Expr.rem(other: Expr): Expr = OperatorApplication(this, "%", other)
public operator fun Expr.rangeTo(other: Expr): Expr = OperatorApplication(this, "..", other)
public operator fun Expr.get(key: Expr, vararg more: Expr): Expr = MapAccess(this, listOf(key) + more)
public infix fun Expr.contains(other: Expr): Expr = OperatorApplication(this, "in", other)
public infix fun Expr.orElse(other: Expr): Expr = OperatorApplication(this, "?:", other)
public infix fun Expr.eq(other: Expr): Expr = OperatorApplication(this, "==", other)
public infix fun Expr.less(other: Expr): Expr = OperatorApplication(this, "<", other)
public infix fun Expr.lessOrEqual(other: Expr): Expr = OperatorApplication(this, "<=", other)
public infix fun Expr.greater(other: Expr): Expr = OperatorApplication(this, ">", other)
public infix fun Expr.greaterOrEqual(other: Expr): Expr = OperatorApplication(this, ">=", other)
public operator fun Expr.unaryPlus(): Expr = UnaryOperatorApplication(this, "+")
public operator fun Expr.unaryMinus(): Expr = UnaryOperatorApplication(this, "-")
public operator fun Expr.not(): Expr = UnaryOperatorApplication(this, "!")
public operator fun Expr.inc(): Expr = UnaryOperatorApplication(this, "++", after = true)
public operator fun Expr.dec(): Expr = UnaryOperatorApplication(this, "--", after = true)
public infix fun Expr.`is`(type: Type): Expr = InstanceCheck(this, type)
public infix fun Expr.`is`(raw: String): Expr = `is`(type(raw))
public infix fun Expr.`as`(type: Type): Expr = TypeCast(this, type, safe = false)
public infix fun Expr.`as?`(type: Type): Expr = TypeCast(this, type, safe = true)
public infix fun Expr.`as`(raw: String): Expr = `as`(type(raw))
public infix fun Expr.`as?`(raw: String): Expr = `as?`(type(raw))

public val Expr.p: Expr get() = ParenthesizedExpr(this)

public fun KotlinRobot.`if`(condition: Expr): IfExpr = IfExpr(imports, condition, null, null)

public infix fun IfExpr.then(expr: Expr): IfExpr {
    then = Body(listOf(expr))
    return this
}

public inline infix fun IfExpr.then(block: BlockRobot.() -> Unit): IfExpr {
    then = makeBody(imports, block)
    return this
}

public infix fun IfExpr.`else`(expr: Expr): IfExpr {
    `else` = Body(listOf(expr))
    return this
}

public inline infix fun IfExpr.`else`(block: BlockRobot.() -> Unit): IfExpr {
    `else` = makeBody(imports, block)
    return this
}

public inline fun KotlinRobot.`when`(body: WhenExprRobot.() -> Unit): Expr = WhenExprRobot(imports).apply(body).finish()

public inline fun KotlinRobot.`when`(subject: Expr, body: SubjectWhenExprRobot.() -> Unit): Expr =
    SubjectWhenExprRobot(imports, subject).apply(body).finish()