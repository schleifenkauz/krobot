/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*
import krobot.impl.IndentedWriter

abstract class BasicDeclarationsRobot @PublishedApi internal constructor(
    imports: ImportsCollector,
    private val declarations: MutableList<in Declaration> = mutableListOf()
) : KotlinRobot(imports) {
    @PublishedApi
    internal fun <D : Declaration> add(declaration: D): D {
        declarations.add(declaration)
        return declaration
    }

    fun Modifiers.`fun`(typeParameters: List<TypeParameter>, name: String, parameters: List<Parameter>): Fun =
        Fun(imports, name, modifiers, typeParameters, parameters = parameters)

    fun Modifiers.`fun`(typeParameters: List<TypeParameter>, name: String, vararg parameters: Parameter): Fun =
        `fun`(typeParameters, name, parameters.asList())

    fun Modifiers.`fun`(name: String, parameters: List<Parameter>): Fun = `fun`(emptyList(), name, parameters)

    fun Modifiers.`fun`(name: String, vararg parameters: Parameter): Fun =
        `fun`(emptyList(), name, parameters.asList())

    fun Type.`fun`(typeParameters: List<TypeParameter>, name: String, parameters: List<Parameter>): Fun =
        Fun(imports, name, modifiers, typeParameters, parameters = parameters, receiver = this)

    fun Type.`fun`(typeParameters: List<TypeParameter>, name: String, vararg parameters: Parameter): Fun =
        `fun`(typeParameters, name, parameters.asList())

    fun Type.`fun`(name: String, parameters: List<Parameter>): Fun = `fun`(emptyList(), name, parameters)

    fun Type.`fun`(name: String, vararg parameters: Parameter): Fun =
        `fun`(emptyList(), name, parameters.asList())

    fun Modifiers.`class`(
        name: String,
        typeParameters: List<TypeParameter>
    ): ClassDefinition<DeclarationType.Class> =
        ClassDefinition(imports, modifiers, DeclarationType.Class, name, typeParameters.toMutableList())

    fun Modifiers.`class`(
        name: String,
        vararg typeParameters: TypeParameter
    ): ClassDefinition<DeclarationType.Class> = `class`(name, typeParameters.asList())

    fun Modifiers.`interface`(
        name: String,
        typeParameters: List<TypeParameter>
    ): ClassDefinition<DeclarationType.Interface> =
        ClassDefinition(imports, modifiers, DeclarationType.Interface, name, typeParameters.toMutableList())

    fun Modifiers.`interface`(
        name: String,
        vararg typeParameters: TypeParameter
    ): ClassDefinition<DeclarationType.Interface> = `interface`(name, typeParameters.asList())

    fun Modifiers.`object`(name: String): ClassDefinition<DeclarationType.Object> =
        ClassDefinition(imports, modifiers, DeclarationType.Object, name)

    fun Modifiers.enum(name: String): ClassDefinition<DeclarationType.Enum> =
        ClassDefinition(imports, modifiers, DeclarationType.Enum, name, enumEntries = mutableListOf())

    fun comment(content: String): Declaration = SingleLineComment(content)

    fun kdoc(vararg lines: String): Declaration {
        return MultiLineComment(lines.asList(), true)
    }

    fun multilineComment(vararg lines: String): Declaration = MultiLineComment(lines.asList(), false)

    fun write(block: IndentedWriter.() -> Unit) {
        add(UserDefinedElement(block))
    }

    operator fun <D : Declaration> D.unaryPlus(): D = add(this)

    operator fun UniversalElement.unaryPlus(): UniversalElement = add(this)

    operator fun String.unaryPlus() {
        add(RawElement(this))
    }
}