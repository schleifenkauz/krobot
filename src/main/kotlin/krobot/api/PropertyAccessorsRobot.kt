/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

class PropertyAccessorsRobot @PublishedApi internal constructor(
    @PublishedApi internal val property: AdvancedProperty
) : KotlinRobot(property.imports) {
    var Modifiers.get: Expr
        get() {
            property.getter = Getter(modifiers, null)
            return DummyExpr
        }
        set(value) {
            property.getter = Getter(modifiers, FunctionBody.SingleExpr(value))
        }

    inline infix fun Modifiers.get(block: AccessorBlockRobot.() -> Unit) {
        val body = AccessorBlockRobot(imports).apply(block).finish()
        property.getter = Getter(modifiers, FunctionBody.Block(body))
    }

    var Modifiers.set: Expr
        get() {
            property.setter = Setter(modifiers, "value", null)
            return DummyExpr
        }
        set(value) {
            property.setter = Setter(modifiers, "value", FunctionBody.SingleExpr(value))
        }

    inline fun Modifiers.set(parameterName: String, block: AccessorBlockRobot.() -> Unit) {
        val body = AccessorBlockRobot(imports).apply(block).finish()
        property.setter = Setter(modifiers, parameterName, FunctionBody.Block(body))
    }

    inline infix fun Modifiers.set(block: AccessorBlockRobot.() -> Unit) {
        set("value", block)
    }
}