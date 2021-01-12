package krobot.api

import krobot.ast.*

/* ---------------------------------------
Classes
  --------------------------------------- */
public fun ClassDefinition.typeParameters(parameters: List<TypeParameter>): ClassDefinition = apply {
    typeParameters.addAll(parameters)
}

public fun ClassDefinition.typeParameters(vararg typeParameter: TypeParameter): ClassDefinition =
    typeParameters(typeParameter.asList())

public infix fun ClassDefinition.typeParameter(param: TypeParameter): ClassDefinition = typeParameters(param)

public infix fun ClassDefinition.typeParameter(name: String): ClassDefinition = typeParameter(invariant(name))

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

public inline infix fun ClassDefinition.body(block: ClassRobot.() -> Unit): ClassDefinition = apply {
    declarations = ClassRobot(imports).apply(block).declarations()
}