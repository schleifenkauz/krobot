/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public infix fun Import.As(alias: String): Import = copy(alias = alias)

public infix fun TopLevelElement.named(name: Identifier): NamedTopLevelElement = NamedTopLevelElement(name, this)

public inline fun kotlinFile(block: KotlinFileRobot.() -> Unit): KotlinFile =
    KotlinFileRobot().apply(block).finishFile()

public inline fun kotlinScript(block: BlockRobot.() -> Unit): KotlinScript =
    KotlinScriptRobot().apply(block).finishScript()

public fun Modifiers.kotlinClass(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition =
    ClassDefinition(ImportsCollector(), modifiers, "class", name, typeParameters.toMutableList())

public fun Modifiers.kotlinClass(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition =
    kotlinClass(name, typeParameters.asList())

public fun kotlinClass(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition =
    noModifiers.kotlinClass(name, typeParameters)

public fun kotlinClass(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition =
    noModifiers.kotlinClass(name, *typeParameters)

public fun Modifiers.kotlinInterface(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition =
    ClassDefinition(ImportsCollector(), modifiers, "interface", name, typeParameters.toMutableList())

public fun Modifiers.kotlinInterface(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition =
    kotlinInterface(name, typeParameters.asList())

public fun kotlinInterface(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition =
    noModifiers.kotlinInterface(name, typeParameters)

public fun kotlinInterface(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition =
    noModifiers.kotlinInterface(name, *typeParameters)

public fun Modifiers.kotlinObject(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition =
    ClassDefinition(ImportsCollector(), modifiers, "object", name, typeParameters.toMutableList())

public fun Modifiers.kotlinObject(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition =
    kotlinObject(name, typeParameters.asList())

public fun kotlinObject(name: Identifier, typeParameters: List<TypeParameter>): ClassDefinition =
    noModifiers.kotlinObject(name, typeParameters)

public fun kotlinObject(name: Identifier, vararg typeParameters: TypeParameter): ClassDefinition =
    noModifiers.kotlinObject(name, *typeParameters)

public inline fun ClassDefinition.asFile(block: KotlinFileRobot.() -> Unit = {}): NamedTopLevelElement = kotlinFile {
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
