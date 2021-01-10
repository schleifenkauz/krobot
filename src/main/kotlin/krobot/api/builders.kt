/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.*
import krobot.ast.Variance.*

/* ---------------------------------------
Utility functions
  --------------------------------------- */
@PublishedApi internal inline fun makeBody(
    imports: ImportsCollector,
    block: BlockRobot.() -> Unit
) = BlockRobot(imports).apply(block).finish()


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

public infix fun TypeParameter.lowerBound(type: Type): TypeParameter = copy(lowerBound = type)

public infix fun TypeParameter.lowerBound(raw: String): TypeParameter = lowerBound(type(raw))

/* ---------------------------------------
Type projections
  --------------------------------------- */
public fun `in`(type: Type): TypeProjection = VarianceTypeProjection(IN, type)

public fun out(type: Type): TypeProjection = VarianceTypeProjection(OUT, type)

/* ---------------------------------------
Properties
  --------------------------------------- */
public infix fun <P : BasicProperty> P.of(type: Type): P = apply {
    this.type = type
}

public infix fun <P : BasicProperty> P.of(raw: String): P = of(type(raw))

public infix fun <P : BasicProperty> P.initializedWith(value: Expr): P = apply {
    initializer = PropertyInitializer.Value(value)
}

public infix fun <P : BasicProperty> P.by(delegate: Expr) {
    initializer = PropertyInitializer.Delegated(delegate)
}

public infix fun AdvancedProperty.receiver(type: Type): AdvancedProperty = apply {
    receiver = type
}

public infix fun AdvancedProperty.receiver(raw: String): AdvancedProperty = receiver(type(raw))

public inline infix fun AdvancedProperty.accessors(block: PropertyAccessorsRobot.() -> Unit) {
    PropertyAccessorsRobot(this).block()
}

public inline operator fun AdvancedProperty.invoke(block: PropertyAccessorsRobot.() -> Unit) {
    accessors(block)
}

/* ---------------------------------------
Functions
  --------------------------------------- */
public fun Fun.parameters(parameters: List<Parameter>): Fun = apply {
    this.parameters = parameters
}

public fun Fun.parameters(vararg parameters: Parameter): Fun = parameters(parameters.asList())

public infix fun Fun.receiver(type: Type): Fun = apply {
    receiver = type
}

public infix fun Fun.receiver(raw: String): Fun = receiver(krobot.api.type(raw))

public infix fun Fun.returnType(type: Type): Fun = apply {
    returnType = type
}

public infix fun Fun.returnType(raw: String): Fun = receiver(type(raw))

public infix fun Fun.body(block: BlockRobot.() -> Unit): Fun = apply {
    val imports = imports
    body = FunctionBody.Block(makeBody(imports, block))
}

public infix fun Fun.returns(expr: Expr): Fun = apply {
    body = FunctionBody.SingleExpr(expr)
}

/* ---------------------------------------
Classes
  --------------------------------------- */
public fun ClassDefinition.primaryConstructor(modifiers: Modifiers, parameters: List<Parameter>): ClassDefinition {
    constructorModifiers = modifiers.modifiers
    constructorParameters = parameters
    return this
}

public fun ClassDefinition.primaryConstructor(modifiers: Modifiers, vararg parameters: Parameter): ClassDefinition =
    primaryConstructor(modifiers, parameters.asList())

public fun ClassDefinition.primaryConstructor(parameters: List<Parameter>): ClassDefinition =
    primaryConstructor(Modifiers(), parameters)

public fun ClassDefinition.primaryConstructor(vararg parameters: Parameter): ClassDefinition =
    primaryConstructor(parameters.asList())

public fun ClassDefinition.extends(type: Type, arguments: List<Expr>?): ClassDefinition = apply {
    supertypes.add(Supertype(type, arguments, null))
}

public fun ClassDefinition.extends(raw: String, arguments: List<Expr>?): ClassDefinition = extends(type(raw), arguments)

public fun ClassDefinition.extends(type: Type, vararg arguments: Expr): ClassDefinition =
    extends(type, arguments.asList())

public fun ClassDefinition.extends(raw: String, vararg arguments: Expr): ClassDefinition =
    extends(raw, arguments.asList())

public fun ClassDefinition.implements(type: Type, by: Expr? = null): ClassDefinition = apply {
    supertypes.add(Supertype(type, null, by))
}

public fun ClassDefinition.implements(raw: String, by: Expr? = null): ClassDefinition = implements(type(raw), by)

public inline infix fun ClassDefinition.body(block: ClassRobot.() -> Unit) {
    declarations = ClassRobot(imports).apply(block).declarations()
}

public inline operator fun ClassDefinition.invoke(block: ClassRobot.() -> Unit) {
    body(block)
}

/* ---------------------------------------
Imports
  --------------------------------------- */
public infix fun Import.As(alias: String): Import = copy(alias = alias)

/* ---------------------------------------
Constructors
  --------------------------------------- */
public infix fun Constructor.delegate(arguments: List<Expr>): Constructor = apply {
    delegationArguments = arguments
}

public fun Constructor.delegate(vararg arguments: Expr): Constructor = delegate(arguments.asList())

public inline infix fun Constructor.body(block: BlockRobot.() -> Unit): Constructor = apply {
    body = makeBody(imports, block).statements
}

public inline operator fun Constructor.invoke(block: BlockRobot.() -> Unit): Constructor = body(block)