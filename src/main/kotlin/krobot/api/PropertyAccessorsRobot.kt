/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public class PropertyAccessorsRobot @PublishedApi internal constructor(
    @PublishedApi internal val property: AdvancedProperty
) : KotlinRobot(property.imports) {
    public var Modifiers.get: Expr
        get() {
            property.getter = Getter(modifiers, null)
            return DummyExpr
        }
        set(value) {
            property.getter = Getter(modifiers, FunctionBody.SingleExpr(value))
        }

    public inline infix fun Modifiers.get(block: AccessorBlockRobot.() -> Unit) {
        val body = AccessorBlockRobot(imports).apply(block).finish()
        property.getter = Getter(modifiers, FunctionBody.Block(body))
    }

    public var Modifiers.set: Expr
        get() {
            property.setter = Setter(modifiers, "value", null)
            return DummyExpr
        }
        set(value) {
            property.setter = Setter(modifiers, "value", FunctionBody.SingleExpr(value))
        }

    public inline fun Modifiers.set(parameterName: String, block: AccessorBlockRobot.() -> Unit) {
        val body = AccessorBlockRobot(imports).apply(block).finish()
        property.setter = Setter(modifiers, parameterName, FunctionBody.Block(body))
    }

    public inline infix fun Modifiers.set(block: AccessorBlockRobot.() -> Unit) {
        set("value", block)
    }
}