/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

public open class Modifiers internal constructor(@PublishedApi internal val modifiers: List<Modifier> = emptyList())

private fun Modifiers.add(modifier: Modifier) = Modifiers(modifiers + modifier)

private fun Modifiers.add(modifier: String) = add(krobot.ast.KeywordModifier(modifier))

public val Modifiers.public: Modifiers
    get() = add("public")
public val Modifiers.protected: Modifiers
    get() = add("protected")
public val Modifiers.private: Modifiers
    get() = add("private")
public val Modifiers.internal: Modifiers
    get() = add("internal")
public val Modifiers.expect: Modifiers
    get() = add("expect")
public val Modifiers.actual: Modifiers
    get() = add("actual")
public val Modifiers.final: Modifiers
    get() = add("final")
public val Modifiers.open: Modifiers
    get() = add("open")
public val Modifiers.abstract: Modifiers
    get() = add("abstract")
public val Modifiers.sealed: Modifiers
    get() = add("sealed")
public val Modifiers.const: Modifiers
    get() = add("const")
public val Modifiers.external: Modifiers
    get() = add("external")
public val Modifiers.override: Modifiers
    get() = add("override")
public val Modifiers.lateinit: Modifiers
    get() = add("lateinit")
public val Modifiers.tailrec: Modifiers
    get() = add("tailrec")
public val Modifiers.vararg: Modifiers
    get() = add("vararg")
public val Modifiers.noinline: Modifiers
    get() = add("noinline")
public val Modifiers.crossinline: Modifiers
    get() = add("crossinline")
public val Modifiers.suspend: Modifiers
    get() = add("suspend")
public val Modifiers.inner: Modifiers
    get() = add("inner")
public val Modifiers.enum: Modifiers
    get() = add("enum")
public val Modifiers.annotation: Modifiers
    get() = add("annotation")
public val Modifiers.`fun`: Modifiers
    get() = add("fun")
public val Modifiers.companion: Modifiers
    get() = add("companion")
public val Modifiers.inline: Modifiers
    get() = add("inline")
public val Modifiers.infix: Modifiers
    get() = add("infix")
public val Modifiers.operator: Modifiers
    get() = add("operator")
public val Modifiers.data: Modifiers
    get() = add("data")
public val Modifiers.`val`: Modifiers
    get() = add("val")
public val Modifiers.`var`: Modifiers
    get() = add("var")

public fun Modifiers.`@`(clazz: String, vararg arguments: Expr): Modifiers =
    add(AnnotationModifier(clazz, arguments.asList()))
