/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public infix fun Import.`as`(alias: String): Import = copy(alias = alias)

public infix fun TopLevelElement.named(name: Identifier): NamedTopLevelElement = NamedTopLevelElement(name, this)

public inline fun kotlinFile(block: KotlinFileRobot.() -> Unit): KotlinFile =
    KotlinFileRobot().apply(block).finishFile()

public inline fun kotlinScript(block: BlockRobot.() -> Unit): KotlinScript =
    KotlinScriptRobot().apply(block).finishScript()

public fun Modifiers.kotlinClass(
    name: Identifier,
    typeParameters: List<TypeParameter>
): ClassDefinition<DeclarationType.Class> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Class, name, typeParameters.toMutableList())

public fun Modifiers.kotlinClass(
    name: Identifier,
    vararg typeParameters: TypeParameter
): ClassDefinition<DeclarationType.Class> = kotlinClass(name, typeParameters.asList())

public fun kotlinClass(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition<DeclarationType.Class> =
    modifiers.kotlinClass(name, typeParameters)

public fun kotlinClass(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition<DeclarationType.Class> =
    modifiers.kotlinClass(name, *typeParameters)

public fun Modifiers.kotlinInterface(
    name: Identifier,
    typeParameters: List<TypeParameter>
): ClassDefinition<DeclarationType.Interface> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Interface, name, typeParameters.toMutableList())

public fun Modifiers.kotlinInterface(
    name: Identifier,
    vararg typeParameters: TypeParameter
): ClassDefinition<DeclarationType.Interface> = kotlinInterface(name, typeParameters.asList())

public fun kotlinInterface(
    name: Identifier,
    typeParameters: List<TypeParameter>
): ClassDefinition<DeclarationType.Interface> = modifiers.kotlinInterface(name, typeParameters)

public fun kotlinInterface(
    name: Identifier,
    vararg typeParameters: TypeParameter
): ClassDefinition<DeclarationType.Interface> = modifiers.kotlinInterface(name, *typeParameters)

public fun Modifiers.kotlinObject(
    name: Identifier
): ClassDefinition<DeclarationType.Object> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Object, name)

public fun kotlinObject(name: Identifier): ClassDefinition<DeclarationType.Object> = modifiers.kotlinObject(name)

public fun Modifiers.kotlinEnum(
    name: Identifier
): ClassDefinition<DeclarationType.Enum> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Enum, name, enumEntries = mutableListOf())

public fun kotlinEnum(name: Identifier): ClassDefinition<DeclarationType.Enum> = modifiers.kotlinEnum(name)

public inline fun ClassDefinition<*>.asFile(block: KotlinFileRobot.() -> Unit = {}): NamedTopLevelElement = kotlinFile {
    block()
    imports.addAll(this@asFile.imports)
    add(this@asFile)
}.named(name)

public fun f() {
    kotlinClass("KotlinClass")
        .typeParameter("T")
        .primaryConstructor("text" of "String")
        .body {
            `val`("x") of "Int" by "lazy"(closure { "1 + 2".e })
        }
        .asFile()
        .saveToSourceRoot("src/main/kotlin")
}
