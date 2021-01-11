/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public class KotlinFileRobot @PublishedApi internal constructor() :
    AdvancedDeclarationsRobot(ImportsCollector(), mutableListOf()) {
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

    @PublishedApi internal fun finishFile(): KotlinFile =
        KotlinFile(packageName, imports.finish(), declarations())
}