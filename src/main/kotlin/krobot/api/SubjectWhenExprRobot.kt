/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*
import krobot.ast.SubjectWhenCondition.*

class SubjectWhenExprRobot @PublishedApi internal constructor(
    imports: ImportsCollector,
    private val subject: Expr
) : KotlinRobot(imports) {
    private val entries = mutableListOf<SubjectWhenEntry>()

    @PublishedApi internal fun add(entry: SubjectWhenEntry) {
        entries.add(entry)
    }

    private fun singleCondition(cond: SubjectWhenCondition) = Conditions(listOf(cond))

    fun `in`(collection: Expr): Conditions = singleCondition(ContainedIn(collection))
    fun `is`(type: Type): Conditions = singleCondition(InstanceOf(type))
    fun `is`(raw: String): Conditions = `is`(type(raw))
    fun equalTo(expr: Expr): Conditions = singleCondition(Equals(expr))

    infix fun Conditions.or(other: Conditions): Conditions = Conditions(conditions + other.conditions)
    infix fun Expr.or(other: Conditions): Conditions = equalTo(this) or other
    infix fun Expr.or(other: Expr): Conditions = equalTo(this) or equalTo(other)

    class Conditions internal constructor(@PublishedApi internal val conditions: List<SubjectWhenCondition>)

    infix fun Conditions.then(body: Expr) {
        add(SubjectWhenEntry(conditions, Body(listOf(body))))
    }

    inline infix fun Conditions.then(block: BlockRobot.() -> Unit) {
        add(SubjectWhenEntry(conditions, BlockRobot(imports).apply(block).finish()))
    }

    infix fun Expr.then(body: Expr) {
        equalTo(this).then(body)
    }

    inline infix fun Expr.then(block: BlockRobot.() -> Unit) {
        equalTo(this).then(block)
    }

    infix fun `else`(body: Expr) {
        add(SubjectWhenEntry(null, Body(listOf(body))))
    }

    val `else`: Expr get() = "else".e

    inline infix fun `else`(block: BlockRobot.() -> Unit) {
        add(SubjectWhenEntry(null, BlockRobot(imports).apply(block).finish()))
    }

    @PublishedApi internal fun finish(): SubjectWhenExpr = SubjectWhenExpr(subject, entries)
}