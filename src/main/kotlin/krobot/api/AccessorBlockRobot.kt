/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.Expr

public class AccessorBlockRobot @PublishedApi internal constructor(imports: ImportsCollector) : BlockRobot(imports) {
    public var field: Expr
        get() = get("field")
        set(value) {
            "field" assign value
        }
}