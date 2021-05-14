/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*
import krobot.impl.IndentedWriter

public val Any.t: Type get() = RawElement(toString())

public fun type(block: IndentedWriter.() -> Unit): Type = UserDefinedElement(block)

public fun type(raw: Identifier, arguments: List<TypeProjection>): Type = GenericType(raw, arguments)

public fun type(raw: Identifier, vararg arguments: TypeProjection): Type = type(raw, arguments.asList())

public fun type(raw: Identifier, argument: String, vararg arguments: String): Type {
    val args = listOf(type(argument)) + arguments.map { type(it) }
    return type(raw, args)
}

public fun functionType(parameters: List<Type>, returnType: Type): Type = FunctionType(null, parameters, returnType)

public fun functionType(vararg parameters: Type, returnType: Type): Type =
    FunctionType(null, parameters.asList(), returnType)

public fun Type.functionType(parameters: List<Type>, returnType: Type): Type =
    FunctionType(this, parameters, returnType)

public fun Type.functionType(vararg parameters: Type, returnType: Type): Type =
    functionType(parameters.asList(), returnType)

public fun String.functionType(parameters: List<Type>, returnType: Type): Type =
    type(this).functionType(parameters, returnType)

public fun String.functionType(vararg parameters: Type, returnType: Type): Type =
    functionType(parameters.asList(), returnType)

public val star: TypeProjection get() = StarProjection

public fun Type.nullable(yesOrNo: Boolean = true): Type = when {
    this is NullableType && yesOrNo || this !is NullableType && !yesOrNo -> this
    this is NullableType && !yesOrNo                                     -> wrapped
    else                                                                 -> NullableType(this)
}

public operator fun String.invoke(vararg arguments: TypeProjection): Type = GenericType(this, arguments.asList())