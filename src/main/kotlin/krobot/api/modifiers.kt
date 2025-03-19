/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*

open class Modifiers internal constructor(@PublishedApi internal val modifiers: List<Modifier> = emptyList())

private fun Modifiers.add(modifier: Modifier) = Modifiers(modifiers + modifier)

private fun Modifiers.add(modifier: String) = add(KeywordModifier(modifier))

private fun singleModifier(modifier: String) = Modifiers(listOf(KeywordModifier(modifier)))

val modifiers: Modifiers get() = Modifiers()
val noModifiers: Modifiers get() = Modifiers()

val Modifiers.public: Modifiers
    get() = add("public")
val Modifiers.protected: Modifiers
    get() = add("protected")
val Modifiers.private: Modifiers
    get() = add("private")
val Modifiers.internal: Modifiers
    get() = add("internal")
val Modifiers.expect: Modifiers
    get() = add("expect")
val Modifiers.actual: Modifiers
    get() = add("actual")
val Modifiers.final: Modifiers
    get() = add("final")
val Modifiers.open: Modifiers
    get() = add("open")
val Modifiers.abstract: Modifiers
    get() = add("abstract")
val Modifiers.sealed: Modifiers
    get() = add("sealed")
val Modifiers.const: Modifiers
    get() = add("const")
val Modifiers.external: Modifiers
    get() = add("external")
val Modifiers.override: Modifiers
    get() = add("override")
val Modifiers.lateinit: Modifiers
    get() = add("lateinit")
val Modifiers.tailrec: Modifiers
    get() = add("tailrec")
val Modifiers.vararg: Modifiers
    get() = add("vararg")
val Modifiers.noinline: Modifiers
    get() = add("noinline")
val Modifiers.crossinline: Modifiers
    get() = add("crossinline")
val Modifiers.suspend: Modifiers
    get() = add("suspend")
val Modifiers.inner: Modifiers
    get() = add("inner")
val Modifiers.annotation: Modifiers
    get() = add("annotation")
val Modifiers.`fun`: Modifiers
    get() = add("fun")
val Modifiers.companion: Modifiers
    get() = add("companion")
val Modifiers.inline: Modifiers
    get() = add("inline")
val Modifiers.infix: Modifiers
    get() = add("infix")
val Modifiers.operator: Modifiers
    get() = add("operator")
val Modifiers.data: Modifiers
    get() = add("data")
val Modifiers.`val`: Modifiers
    get() = add("val")
val Modifiers.`var`: Modifiers
    get() = add("var")

val public: Modifiers
    get() = singleModifier("public")
val protected: Modifiers
    get() = singleModifier("protected")
val private: Modifiers
    get() = singleModifier("private")
val internal: Modifiers
    get() = singleModifier("internal")
val expect: Modifiers
    get() = singleModifier("expect")
val actual: Modifiers
    get() = singleModifier("actual")
val final: Modifiers
    get() = singleModifier("final")
val open: Modifiers
    get() = singleModifier("open")
val abstract: Modifiers
    get() = singleModifier("abstract")
val sealed: Modifiers
    get() = singleModifier("sealed")
val const: Modifiers
    get() = singleModifier("const")
val external: Modifiers
    get() = singleModifier("external")
val override: Modifiers
    get() = singleModifier("override")
val lateinit: Modifiers
    get() = singleModifier("lateinit")
val tailrec: Modifiers
    get() = singleModifier("tailrec")
val vararg: Modifiers
    get() = singleModifier("vararg")
val noinline: Modifiers
    get() = singleModifier("noinline")
val crossinline: Modifiers
    get() = singleModifier("crossinline")
val suspend: Modifiers
    get() = singleModifier("suspend")
val inner: Modifiers
    get() = singleModifier("inner")
val annotation: Modifiers
    get() = singleModifier("annotation")
val `fun`: Modifiers
    get() = singleModifier("fun")
val companion: Modifiers
    get() = singleModifier("companion")
val inline: Modifiers
    get() = singleModifier("inline")
val infix: Modifiers
    get() = singleModifier("infix")
val operator: Modifiers
    get() = singleModifier("operator")
val data: Modifiers
    get() = singleModifier("data")
val `val`: Modifiers
    get() = singleModifier("val")
val `var`: Modifiers
    get() = singleModifier("var")

fun Modifiers.`@`(clazz: String, vararg arguments: Expr): Modifiers =
    add(AnnotationModifier(clazz, arguments.asList()))

fun `@`(clazz: String, vararg arguments: Expr): AnnotationModifier = AnnotationModifier(clazz, arguments.asList())