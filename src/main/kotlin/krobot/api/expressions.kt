/**
 * @author Nikolaus Knop
 */

@file: Suppress("DANGEROUS_CHARACTERS")

package krobot.api

import krobot.ast.*
import krobot.impl.IndentedWriter

fun lit(value: Boolean): Expr = Literal("$value")
fun lit(value: Byte): Expr = Literal("${value}.toByte()")
fun lit(value: Short): Expr = Literal("${value}.toShort()")
fun lit(value: Char): Expr = Literal("'$value'")
fun lit(value: Int): Expr = Literal("$value")
fun lit(value: Long): Expr = Literal("${value}l")
fun lit(value: String): Expr = Literal("\"$value\"")
fun lit(value: Double): Expr = Literal("$value")
fun lit(value: Float): Expr = Literal("${value}f")
val `null`: Expr get() = get("null")
val `this`: Expr get() = get("this")
fun `this`(scope: String): Expr = get("this@$scope")
val `super`: Expr get() = get("super")

val Any?.e: Expr get() = RawElement(toString())

fun expr(block: IndentedWriter.() -> Unit): Expr = UserDefinedElement(block)

infix fun Expr.select(name: Identifier): Expr = PropertyAccess(this, name)

fun get(name: Identifier): Expr = PropertyAccess(null, name)

fun call(name: Identifier, typeArguments: List<Type>, arguments: List<Expr>): Expr =
    FunctionCall(null, name, typeArguments, arguments)

fun call(name: Identifier, typeArguments: List<Type>, vararg arguments: Expr): Expr =
    call(name, typeArguments, arguments.asList())

fun call(name: Identifier, arguments: List<Expr>): Expr =
    call(name, emptyList(), arguments)

fun call(name: Identifier, vararg arguments: Expr): Expr =
    call(name, arguments.asList())

operator fun String.invoke(
    typeArguments: List<Type> = emptyList(),
    arguments: List<Expr> = emptyList()
): Expr = FunctionCall(null, this, typeArguments, arguments)

operator fun String.invoke(arguments: List<Expr>): Expr = invoke(emptyList(), arguments)

operator fun String.invoke(vararg arguments: Expr): Expr = invoke(arguments.asList())

fun Expr.call(
    name: Identifier,
    typeArguments: List<Type>,
    arguments: List<Expr>
): Expr = FunctionCall(this, name, typeArguments, arguments)

fun Expr.call(
    name: Identifier,
    typeArguments: List<Type>,
    vararg arguments: Expr
): Expr = call(name, typeArguments, arguments.asList())

fun Expr.call(
    name: Identifier,
    arguments: List<Expr>
): Expr = this.call(name, emptyList(), arguments)

fun Expr.call(name: Identifier, vararg arguments: Expr): Expr =
    this.call(name, arguments.asList())

infix fun Expr.call(name: Identifier): Expr = call(name, emptyList<Type>())

inline fun KotlinRobot.closure(parameters: List<Parameter>, block: BlockRobot.() -> Unit): Expr {
    val body = BlockRobot(imports).apply(block).finish()
    return Closure(parameters, body.statements)
}

@JvmName("closure2")
inline fun KotlinRobot.closure(
    parameters: List<String>,
    block: BlockRobot.() -> Unit
): Expr = closure(parameters.map { Parameter(it) }, block)

inline fun KotlinRobot.closure(vararg parameters: String, block: BlockRobot.() -> Unit): Expr =
    closure(parameters.asList(), block)

inline fun KotlinRobot.closure(vararg parameters: Parameter, block: BlockRobot.() -> Unit): Expr =
    closure(parameters.asList(), block)

inline fun KotlinRobot.closure(block: BlockRobot.() -> Unit): Expr = closure(emptyList<String>(), block)

operator fun Expr.plus(other: Expr): Expr = OperatorApplication(this, "+", other)
operator fun Expr.minus(other: Expr): Expr = OperatorApplication(this, "-", other)
operator fun Expr.times(other: Expr): Expr = OperatorApplication(this, "*", other)
operator fun Expr.div(other: Expr): Expr = OperatorApplication(this, "/", other)
operator fun Expr.rem(other: Expr): Expr = OperatorApplication(this, "%", other)
operator fun Expr.rangeTo(other: Expr): Expr = OperatorApplication(this, "..", other)
operator fun Expr.get(key: Expr, vararg more: Expr): Expr = MapAccess(this, listOf(key) + more)
infix fun Expr.contains(other: Expr): Expr = OperatorApplication(this, "in", other)
infix fun Expr.orElse(other: Expr): Expr = OperatorApplication(this, "?:", other)
infix fun Expr.eq(other: Expr): Expr = OperatorApplication(this, "==", other)
infix fun Expr.less(other: Expr): Expr = OperatorApplication(this, "<", other)
infix fun Expr.lessOrEqual(other: Expr): Expr = OperatorApplication(this, "<=", other)
infix fun Expr.greater(other: Expr): Expr = OperatorApplication(this, ">", other)
infix fun Expr.greaterOrEqual(other: Expr): Expr = OperatorApplication(this, ">=", other)
operator fun Expr.unaryPlus(): Expr = UnaryOperatorApplication(this, "+")
operator fun Expr.unaryMinus(): Expr = UnaryOperatorApplication(this, "-")
operator fun Expr.not(): Expr = UnaryOperatorApplication(this, "!")
operator fun Expr.inc(): Expr = UnaryOperatorApplication(this, "++", after = true)
operator fun Expr.dec(): Expr = UnaryOperatorApplication(this, "--", after = true)
infix fun Expr.`is`(type: Type): Expr = InstanceCheck(this, type)
infix fun Expr.`is`(raw: String): Expr = `is`(type(raw))
infix fun Expr.`as`(type: Type): Expr = TypeCast(this, type, safe = false)
infix fun Expr.`as?`(type: Type): Expr = TypeCast(this, type, safe = true)
infix fun Expr.`as`(raw: String): Expr = `as`(type(raw))
infix fun Expr.`as?`(raw: String): Expr = `as?`(type(raw))

val Expr.p: Expr get() = ParenthesizedExpr(this)

fun KotlinRobot.`if`(condition: Expr): IfExpr = IfExpr(imports, condition, null, null)

infix fun IfExpr.then(expr: Expr): IfExpr {
    then = Body(listOf(expr))
    return this
}

inline infix fun IfExpr.then(block: BlockRobot.() -> Unit): IfExpr {
    then = BlockRobot(imports).apply(block).finish()
    return this
}

infix fun IfExpr.`else`(expr: Expr): IfExpr {
    `else` = Body(listOf(expr))
    return this
}

inline infix fun IfExpr.`else`(block: BlockRobot.() -> Unit): IfExpr {
    `else` = BlockRobot(imports).apply(block).finish()
    return this
}

inline fun KotlinRobot.`when`(body: WhenExprRobot.() -> Unit): Expr = WhenExprRobot(imports).apply(body).finish()

inline fun KotlinRobot.`when`(subject: Expr, body: SubjectWhenExprRobot.() -> Unit): Expr =
    SubjectWhenExprRobot(imports, subject).apply(body).finish()