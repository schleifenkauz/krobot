/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*
import krobot.ast.SubjectWhenCondition.*

public class SubjectWhenExprRobot @PublishedApi internal constructor(
    imports: ImportsCollector,
    private val subject: Expr
) : KotlinRobot(imports) {
    private val entries = mutableListOf<SubjectWhenEntry>()

    @PublishedApi internal fun add(entry: SubjectWhenEntry) {
        entries.add(entry)
    }

    private fun singleCondition(cond: SubjectWhenCondition) = Conditions(listOf(cond))

    public fun `in`(collection: Expr): Conditions = singleCondition(ContainedIn(collection))
    public fun `is`(type: Type): Conditions = singleCondition(InstanceOf(type))
    public fun `is`(raw: String): Conditions = `is`(type(raw))
    public fun equalTo(expr: Expr): Conditions = singleCondition(Equals(expr))

    public infix fun Conditions.or(other: Conditions): Conditions = Conditions(conditions + other.conditions)
    public infix fun Expr.or(other: Conditions): Conditions = equalTo(this) or other
    public infix fun Expr.or(other: Expr): Conditions = equalTo(this) or equalTo(other)

    public class Conditions internal constructor(@PublishedApi internal val conditions: List<SubjectWhenCondition>)

    public infix fun Conditions.then(body: Expr) {
        add(SubjectWhenEntry(conditions, Body(listOf(body))))
    }

    public inline infix fun Conditions.then(block: BlockRobot.() -> Unit) {
        add(SubjectWhenEntry(conditions, makeBody(imports, block)))
    }

    public infix fun Expr.then(body: Expr) {
        equalTo(this).then(body)
    }

    public inline infix fun Expr.then(block: BlockRobot.() -> Unit) {
        equalTo(this).then(block)
    }

    public infix fun `else`(body: Expr) {
        add(SubjectWhenEntry(null, Body(listOf(body))))
    }

    public val `else`: Expr get() = "else".e

    public inline infix fun `else`(block: BlockRobot.() -> Unit) {
        add(SubjectWhenEntry(null, makeBody(imports, block)))
    }

    @PublishedApi internal fun finish(): SubjectWhenExpr = SubjectWhenExpr(subject, entries)
}