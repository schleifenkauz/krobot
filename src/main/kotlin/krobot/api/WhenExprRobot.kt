/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

class WhenExprRobot @PublishedApi internal constructor(imports: ImportsCollector) : KotlinRobot(imports) {
    private val entries = mutableListOf<WhenEntry>()

    @PublishedApi internal fun add(entry: WhenEntry) {
        entries.add(entry)
    }

    infix fun Expr.then(body: Expr) {
        add(WhenEntry(this, Body(listOf(body))))
    }

    inline infix fun Expr.then(block: BlockRobot.() -> Unit) {
        add(WhenEntry(this, BlockRobot(imports).apply(block).finish()))
    }

    infix fun `else`(body: Expr) {
        add(WhenEntry(null, Body(listOf(body))))
    }

    inline infix fun `else`(block: BlockRobot.() -> Unit) {
        add(WhenEntry(null, BlockRobot(imports).apply(block).finish()))
    }

    @PublishedApi internal fun finish(): WhenExpr = WhenExpr(entries)
}