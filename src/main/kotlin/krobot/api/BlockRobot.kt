/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public open class BlockRobot @PublishedApi internal constructor(
    imports: ImportsCollector,
    private val elements: MutableList<BlockElement> = mutableListOf()
) : BasicDeclarationsRobot(imports, elements) {
    @PublishedApi internal fun <T : BlockElement> add(element: T): T {
        elements.add(element)
        return element
    }

    public operator fun Expr.unaryPlus() {
        add(this)
    }

    public inline fun `for`(name: String, type: Type? = null, `in`: Expr, block: BlockRobot.() -> Unit): Statement {
        val body = BlockRobot(imports).apply(block).finish()
        return ForLoop(name, type, `in`, body)
    }

    public inline fun `while`(condition: Expr, block: BlockRobot.() -> Unit): Statement {
        val body = BlockRobot(imports).apply(block).finish()
        return WhileLoop(condition, body)
    }

    public open infix fun Modifiers.`val`(name: String): BasicProperty = BasicProperty(imports, modifiers, "val", name)

    public open infix fun Modifiers.`var`(name: String): BasicProperty = BasicProperty(imports, modifiers, "var", name)

    public infix fun Assignable.assign(value: Expr) {
        add(Assignment(this, value))
    }

    public infix fun String.assign(value: Expr) {
        PropertyAccess(null, this).assign(value)
    }

    private fun Assignable.augAssign(operator: String, value: Expr) {
        add(AugmentedAssignment(this, operator, value))
    }

    public operator fun Assignable.plusAssign(value: Expr) {
        augAssign("+", value)
    }

    public operator fun String.plusAssign(value: Expr) {
        PropertyAccess(null, this) += value
    }

    public operator fun Assignable.minusAssign(value: Expr) {
        augAssign("-", value)
    }

    public operator fun String.minusAssign(value: Expr) {
        PropertyAccess(null, this) -= value
    }

    public operator fun Assignable.timesAssign(value: Expr) {
        augAssign("*", value)
    }

    public operator fun String.timesAssign(value: Expr) {
        PropertyAccess(null, this) *= value
    }

    public operator fun Assignable.divAssign(value: Expr) {
        augAssign("/", value)
    }

    public operator fun String.divAssign(value: Expr) {
        PropertyAccess(null, this) /= value
    }

    public operator fun Assignable.remAssign(value: Expr) {
        augAssign("%", value)
    }

    public operator fun String.remAssign(value: Expr) {
        PropertyAccess(null, this) %= value
    }

    public operator fun <E: BlockElement> E.unaryPlus(): E = add(this)

    @PublishedApi internal fun finish(): Body = Body(elements)
}