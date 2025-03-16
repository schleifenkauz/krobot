/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public open class AdvancedDeclarationsRobot @PublishedApi internal constructor(
    imports: ImportsCollector,
    private val declarations: MutableList<Declaration>
) : BasicDeclarationsRobot(imports, declarations) {
    private fun Modifiers.addProperty(valOrVar: String, name: String, receiver: Type? = null) =
        add(AdvancedProperty(imports, modifiers, valOrVar, name, null, receiver))

    public infix fun Modifiers.`val`(name: String): AdvancedProperty = addProperty("val", name)

    public infix fun Modifiers.`var`(name: String): AdvancedProperty = addProperty("var", name)

    public fun Type.`val`(name: String, modifiers: Modifiers): AdvancedProperty = with(modifiers) {
        addProperty("val", name, receiver = this@`val`)
    }

    public fun Type.`var`(name: String, modifiers: Modifiers): AdvancedProperty = with(modifiers) {
        addProperty("var", name, receiver = this@`var`)
    }

    public infix fun Type.`val`(name: String): AdvancedProperty = `val`(name, Modifiers())

    public infix fun Type.`var`(name: String): AdvancedProperty = `var`(name, Modifiers())

    @PublishedApi internal fun declarations(): List<Declaration> = declarations
}