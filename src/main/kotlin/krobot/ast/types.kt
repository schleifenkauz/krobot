/**
 * @author Nikolaus Knop
 */

package krobot.ast

import krobot.impl.IndentedWriter

sealed interface TypeProjection : Element

sealed interface Type : TypeProjection

internal enum class Variance(private val string: String) {
    IN("in "), OUT("out "), NONE("");

    override fun toString(): String = string
}

internal object StarProjection : TypeProjection {
    override fun append(out: IndentedWriter) {
        out.append("*")
    }
}

internal data class VarianceTypeProjection(
    private val variance: Variance,
    private val type: Type
) : TypeProjection {
    override fun append(out: IndentedWriter) = with(out) {
        append(variance)
        append(type)
    }
}

internal data class GenericType(
    private val raw: Identifier,
    private val arguments: List<TypeProjection>
) : Type {
    override fun append(out: IndentedWriter) = with(out) {
        append(raw)
        join(arguments, ", ", "<", ">")
    }
}

internal data class FunctionType(
    private val receiver: Type?,
    private val parameters: List<Type>,
    private val returnType: Type
) : Type {
    override fun append(out: IndentedWriter) = with(out) {
        join(receiver, ".")
        join(parameters, ", ", "(", ")")
        append(" -> ")
        append(returnType)
    }
}

internal data class NullableType(internal val wrapped: Type) : Type {
    override fun append(out: IndentedWriter) = with(out) {
        if (wrapped is FunctionType) append('(')
        append(wrapped)
        if (wrapped is FunctionType) append(')')
        append('?')
    }
}

internal data class AnnotatedType(internal val type: Type, internal val annotations: List<AnnotationModifier>) : Type {
    override fun append(out: IndentedWriter) {
        out.join(annotations, " ", postfix = " ")
        type.append(out)
    }
}