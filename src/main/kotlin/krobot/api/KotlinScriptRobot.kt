/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public class KotlinScriptRobot @PublishedApi internal constructor() :
    BlockRobot(ImportsCollector()) {
    private var packageName: String? = null

    public fun `package`(name: String?) {
        packageName = name
    }

    public fun Modifiers.typeAlias(name: String, parameters: List<String>, type: Type) {
        add(TypeAlias(modifiers, name, parameters, type))
    }

    public fun Modifiers.typeAlias(name: String, type: Type) {
        typeAlias(name, emptyList(), type)
    }

    @PublishedApi internal fun finishScript(): KotlinScript =
        KotlinScript(packageName, imports.finish(), finish().statements)
}