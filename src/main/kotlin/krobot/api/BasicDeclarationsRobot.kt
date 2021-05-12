/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public abstract class BasicDeclarationsRobot @PublishedApi internal constructor(
    imports: ImportsCollector,
    private val declarations: MutableList<in Declaration> = mutableListOf()
) : KotlinRobot(imports) {
    @PublishedApi internal fun <D : Declaration> add(declaration: D): D {
        declarations.add(declaration)
        return declaration
    }

    public infix fun Modifiers.parameter(name: String): Parameter = Parameter(name, modifiers)

    public fun Modifiers.`fun`(typeParameters: List<TypeParameter>, name: String, parameters: List<Parameter>): Fun =
        add(Fun(imports, name, modifiers, typeParameters, parameters = parameters))

    public fun Modifiers.`fun`(typeParameters: List<TypeParameter>, name: String, vararg parameters: Parameter): Fun =
        `fun`(typeParameters, name, parameters.asList())

    public fun Modifiers.`fun`(name: String, parameters: List<Parameter>): Fun = `fun`(emptyList(), name, parameters)

    public fun Modifiers.`fun`(name: String, vararg parameters: Parameter): Fun =
        `fun`(emptyList(), name, parameters.asList())

    public fun Type.`fun`(typeParameters: List<TypeParameter>, name: String, parameters: List<Parameter>): Fun =
        add(Fun(imports, name, modifiers, typeParameters, parameters = parameters, receiver = this))

    public fun Type.`fun`(typeParameters: List<TypeParameter>, name: String, vararg parameters: Parameter): Fun =
        `fun`(typeParameters, name, parameters.asList())

    public fun Type.`fun`(name: String, parameters: List<Parameter>): Fun = `fun`(emptyList(), name, parameters)

    public fun Type.`fun`(name: String, vararg parameters: Parameter): Fun = `fun`(emptyList(), name, parameters.asList())

    public fun Modifiers.`class`(name: String, typeParameters: List<TypeParameter>): ClassDefinition<DeclarationType.Class> =
        add(ClassDefinition(imports, modifiers, DeclarationType.Class, name, typeParameters.toMutableList()))

    public fun Modifiers.`class`(name: String, vararg typeParameters: TypeParameter): ClassDefinition<DeclarationType.Class> =
        `class`(name, typeParameters.asList())

    public fun Modifiers.`interface`(name: String, typeParameters: List<TypeParameter>): ClassDefinition<DeclarationType.Interface> =
        add(ClassDefinition(imports, modifiers, DeclarationType.Interface, name, typeParameters.toMutableList()))

    public fun Modifiers.`interface`(name: String, vararg typeParameters: TypeParameter): ClassDefinition<DeclarationType.Interface> =
        `interface`(name, typeParameters.asList())

    public fun Modifiers.`object`(name: String): ClassDefinition<DeclarationType.Object> =
        add(ClassDefinition(imports, modifiers, DeclarationType.Object, name))

    public fun Modifiers.enum(name: String): ClassDefinition<DeclarationType.Enum> =
        add(ClassDefinition(imports, modifiers, DeclarationType.Enum, name, enumEntries = mutableListOf()))
}