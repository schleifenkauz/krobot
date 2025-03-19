package krobot.api

import krobot.ast.*
import krobot.ast.Variance.*

/* ---------------------------------------
Parameters
  --------------------------------------- */
fun parameter(name: String): Parameter = Parameter(name)
infix fun String.of(type: Type): Parameter = Parameter(this, type = type)
infix fun String.of(raw: String): Parameter = of(type(raw))
infix fun Parameter.of(type: Type): Parameter = copy(type = type)
infix fun Parameter.default(default: Expr): Parameter = copy(defaultValue = default)

infix fun Modifiers.parameter(name: String): Parameter = Parameter(name, modifiers)

/* ---------------------------------------
Type Parameters
  --------------------------------------- */
fun invariant(name: String): TypeParameter = TypeParameter(NONE, name, null)
fun `in`(name: String): TypeParameter = TypeParameter(IN, name, null)
fun out(name: String): TypeParameter = TypeParameter(OUT, name, null)
infix fun TypeParameter.lowerBound(type: Type?): TypeParameter = copy(lowerBound = type)
infix fun TypeParameter.lowerBound(raw: String): TypeParameter = lowerBound(type(raw))

/* ---------------------------------------
Type projections
  --------------------------------------- */
fun `in`(type: Type): TypeProjection = VarianceTypeProjection(IN, type)
fun out(type: Type): TypeProjection = VarianceTypeProjection(OUT, type)

