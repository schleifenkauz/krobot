/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public class KotlinScriptRobot @PublishedApi internal constructor() : BlockRobot(ImportsCollector()) {
    private var packageName: String? = null

    public fun `package`(name: String?) {
        packageName = name
    }

    public fun Modifiers.typeAlias(name: String, parameters: List<String>, type: Type): Declaration =
        TypeAlias(modifiers, name, parameters, type)

    public fun Modifiers.typeAlias(name: String, type: Type): Declaration = typeAlias(name, emptyList(), type)

    @PublishedApi
    internal fun finishScript(): KotlinScript =
        KotlinScript(packageName, imports.finish(), finish().statements)

    public override infix fun Modifiers.`val`(name: String): AdvancedProperty =
        AdvancedProperty(imports, modifiers, "val", name, null)

    public override infix fun Modifiers.`var`(name: String): AdvancedProperty =
        AdvancedProperty(imports, modifiers, "val", name, null)
}