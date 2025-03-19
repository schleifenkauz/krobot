/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.Expr

class AccessorBlockRobot @PublishedApi internal constructor(imports: ImportsCollector) : BlockRobot(imports) {
    var field: Expr
        get() = get("field")
        set(value) {
            "field" assign value
        }
}