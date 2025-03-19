/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

infix fun Import.`as`(alias: String): Import = copy(alias = alias)

infix fun TopLevelElement.named(name: Identifier): NamedTopLevelElement = NamedTopLevelElement(name, this)

inline fun kotlinFile(block: KotlinFileRobot.() -> Unit): KotlinFile =
    KotlinFileRobot().apply(block).finishFile()

inline fun kotlinScript(block: BlockRobot.() -> Unit): KotlinScript =
    KotlinScriptRobot().apply(block).finishScript()

fun Modifiers.kotlinClass(
    name: Identifier,
    typeParameters: List<TypeParameter>
): ClassDefinition<DeclarationType.Class> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Class, name, typeParameters.toMutableList())

fun Modifiers.kotlinClass(
    name: Identifier,
    vararg typeParameters: TypeParameter
): ClassDefinition<DeclarationType.Class> = kotlinClass(name, typeParameters.asList())

fun kotlinClass(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition<DeclarationType.Class> =
    modifiers.kotlinClass(name, typeParameters)

fun kotlinClass(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition<DeclarationType.Class> =
    modifiers.kotlinClass(name, *typeParameters)

fun Modifiers.kotlinInterface(
    name: Identifier,
    typeParameters: List<TypeParameter>
): ClassDefinition<DeclarationType.Interface> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Interface, name, typeParameters.toMutableList())

fun Modifiers.kotlinInterface(
    name: Identifier,
    vararg typeParameters: TypeParameter
): ClassDefinition<DeclarationType.Interface> = kotlinInterface(name, typeParameters.asList())

fun kotlinInterface(
    name: Identifier,
    typeParameters: List<TypeParameter>
): ClassDefinition<DeclarationType.Interface> = modifiers.kotlinInterface(name, typeParameters)

fun kotlinInterface(
    name: Identifier,
    vararg typeParameters: TypeParameter
): ClassDefinition<DeclarationType.Interface> = modifiers.kotlinInterface(name, *typeParameters)

fun Modifiers.kotlinObject(
    name: Identifier
): ClassDefinition<DeclarationType.Object> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Object, name)

fun kotlinObject(name: Identifier): ClassDefinition<DeclarationType.Object> = modifiers.kotlinObject(name)

fun Modifiers.kotlinEnum(
    name: Identifier
): ClassDefinition<DeclarationType.Enum> =
    ClassDefinition(ImportsCollector(), modifiers, DeclarationType.Enum, name, enumEntries = mutableListOf())

fun kotlinEnum(name: Identifier): ClassDefinition<DeclarationType.Enum> = modifiers.kotlinEnum(name)

inline fun ClassDefinition<*>.asFile(block: KotlinFileRobot.() -> Unit = {}): NamedTopLevelElement = kotlinFile {
    block()
    imports.addAll(this@asFile.imports)
    add(this@asFile)
}.named(name)

fun f() {
    kotlinClass("KotlinClass")
        .typeParameter("T")
        .primaryConstructor("text" of "String")
        .body {
            `val`("x") of "Int" by "lazy"(closure { "1 + 2".e })
        }
        .asFile()
        .saveToSourceRoot("src/main/kotlin")
}
