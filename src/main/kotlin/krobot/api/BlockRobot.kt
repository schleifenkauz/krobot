/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

open class BlockRobot @PublishedApi internal constructor(
    imports: ImportsCollector,
    private val elements: MutableList<BlockElement> = mutableListOf()
) : BasicDeclarationsRobot(imports, elements) {
    @PublishedApi internal fun <T : BlockElement> add(element: T): T {
        elements.add(element)
        return element
    }

    operator fun Expr.unaryPlus() {
        add(this)
    }

    inline fun `for`(name: String, type: Type? = null, `in`: Expr, block: BlockRobot.() -> Unit): Statement {
        val body = BlockRobot(imports).apply(block).finish()
        return ForLoop(name, type, `in`, body)
    }

    inline fun `while`(condition: Expr, block: BlockRobot.() -> Unit): Statement {
        val body = BlockRobot(imports).apply(block).finish()
        return WhileLoop(condition, body)
    }

    open infix fun Modifiers.`val`(name: String): BasicProperty = BasicProperty(imports, modifiers, "val", name)

    open infix fun Modifiers.`var`(name: String): BasicProperty = BasicProperty(imports, modifiers, "var", name)

    infix fun Assignable.assign(value: Expr) {
        add(Assignment(this, value))
    }

    infix fun String.assign(value: Expr) {
        PropertyAccess(null, this).assign(value)
    }

    private fun Assignable.augAssign(operator: String, value: Expr) {
        add(AugmentedAssignment(this, operator, value))
    }

    operator fun Assignable.plusAssign(value: Expr) {
        augAssign("+", value)
    }

    operator fun String.plusAssign(value: Expr) {
        PropertyAccess(null, this) += value
    }

    operator fun Assignable.minusAssign(value: Expr) {
        augAssign("-", value)
    }

    operator fun String.minusAssign(value: Expr) {
        PropertyAccess(null, this) -= value
    }

    operator fun Assignable.timesAssign(value: Expr) {
        augAssign("*", value)
    }

    operator fun String.timesAssign(value: Expr) {
        PropertyAccess(null, this) *= value
    }

    operator fun Assignable.divAssign(value: Expr) {
        augAssign("/", value)
    }

    operator fun String.divAssign(value: Expr) {
        PropertyAccess(null, this) /= value
    }

    operator fun Assignable.remAssign(value: Expr) {
        augAssign("%", value)
    }

    operator fun String.remAssign(value: Expr) {
        PropertyAccess(null, this) %= value
    }

    operator fun <E: BlockElement> E.unaryPlus(): E = add(this)

    @PublishedApi internal fun finish(): Body = Body(elements)
}