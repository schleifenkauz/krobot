/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.Constructor
import krobot.ast.InitializerBlock
import krobot.ast.Parameter

open class ClassRobot @PublishedApi internal constructor(imports: ImportsCollector) :
    AdvancedDeclarationsRobot(imports, mutableListOf()) {
    fun Modifiers.constructor(parameters: List<Parameter>): Constructor =
        Constructor(imports, modifiers, parameters, null, null)

    fun Modifiers.constructor(vararg parameters: Parameter): Constructor = constructor(parameters.asList())

    fun init(block: BlockRobot.() -> Unit) {
        val body = BlockRobot(imports).apply(block).finish()
        val initializer = InitializerBlock(body)
        add(initializer)
    }
}