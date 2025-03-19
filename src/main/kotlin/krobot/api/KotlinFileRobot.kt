/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

class KotlinFileRobot @PublishedApi internal constructor() :
    AdvancedDeclarationsRobot(ImportsCollector(), mutableListOf()) {
    private var packageName: String? = null

    fun `package`(name: String?) {
        packageName = name
    }

    fun Modifiers.typeAlias(name: String, parameters: List<String>, type: Type): Declaration =
        TypeAlias(modifiers, name, parameters, type)

    fun Modifiers.typeAlias(name: String, type: Type): Declaration = typeAlias(name, emptyList(), type)

    @PublishedApi
    internal fun finishFile(): KotlinFile =
        KotlinFile(packageName, imports.finish(), declarations())
}