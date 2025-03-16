/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*
import krobot.impl.IndentedWriter

val Any.t: Type get() = RawElement(toString())

fun type(block: IndentedWriter.() -> Unit): Type = UserDefinedElement(block)

fun type(raw: Identifier, arguments: List<TypeProjection>): Type = GenericType(raw, arguments)

fun type(raw: Identifier, vararg arguments: TypeProjection): Type = type(raw, arguments.asList())

fun type(raw: Identifier, argument: String, vararg arguments: String): Type {
    val args = listOf(type(argument)) + arguments.map { type(it) }
    return type(raw, args)
}

fun functionType(parameters: List<Type>, returnType: Type): Type = FunctionType(null, parameters, returnType)

fun functionType(vararg parameters: Type, returnType: Type): Type =
    FunctionType(null, parameters.asList(), returnType)

fun Type.functionType(parameters: List<Type>, returnType: Type): Type =
    FunctionType(this, parameters, returnType)

fun Type.functionType(vararg parameters: Type, returnType: Type): Type =
    functionType(parameters.asList(), returnType)

fun String.functionType(parameters: List<Type>, returnType: Type): Type =
    type(this).functionType(parameters, returnType)

fun String.functionType(vararg parameters: Type, returnType: Type): Type =
    functionType(parameters.asList(), returnType)

val star: TypeProjection get() = StarProjection

fun Type.nullable(yesOrNo: Boolean = true): Type = when {
    this is NullableType && yesOrNo || this !is NullableType && !yesOrNo -> this
    this is NullableType -> wrapped
    else -> NullableType(this)
}

operator fun String.invoke(vararg arguments: TypeProjection): Type = GenericType(this, arguments.asList())

fun Type.annotated(vararg annotations: AnnotationModifier): Type = when (this) {
    is AnnotatedType -> copy(annotations = this.annotations + annotations)
    else -> AnnotatedType(this, annotations.asList())
}