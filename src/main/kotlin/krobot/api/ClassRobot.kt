/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.Constructor
import krobot.ast.Parameter

public open class ClassRobot @PublishedApi internal constructor(imports: ImportsCollector) :
    AdvancedDeclarationsRobot(imports, mutableListOf()) {
    public fun Modifiers.constructor(parameters: List<Parameter>): Constructor =
        add(Constructor(imports, modifiers, parameters, null, null))

    public fun Modifiers.constructor(vararg parameters: Parameter): Constructor = constructor(parameters.asList())
}