package krobot.api

import krobot.ast.*
import krobot.ast.Variance.*

/* ---------------------------------------
Parameters
  --------------------------------------- */
public fun parameter(name: String): Parameter = Parameter(name)
public infix fun String.of(type: Type): Parameter = Parameter(this, type = type)
public infix fun String.of(raw: String): Parameter = of(type(raw))
public infix fun Parameter.of(type: Type): Parameter = copy(type = type)
public infix fun Parameter.default(default: Expr): Parameter = copy(defaultValue = default)

/* ---------------------------------------
Type Parameters
  --------------------------------------- */
public fun invariant(name: String): TypeParameter = TypeParameter(NONE, name, null)
public fun `in`(name: String): TypeParameter = TypeParameter(IN, name, null)
public fun out(name: String): TypeParameter = TypeParameter(OUT, name, null)
public infix fun TypeParameter.lowerBound(type: Type?): TypeParameter = copy(lowerBound = type)
public infix fun TypeParameter.lowerBound(raw: String): TypeParameter = lowerBound(type(raw))

/* ---------------------------------------
Type projections
  --------------------------------------- */
public fun `in`(type: Type): TypeProjection = VarianceTypeProjection(IN, type)
public fun out(type: Type): TypeProjection = VarianceTypeProjection(OUT, type)