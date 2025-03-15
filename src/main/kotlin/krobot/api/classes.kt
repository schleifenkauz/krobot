package krobot.api

import krobot.ast.*

/* ---------------------------------------
Classes
  --------------------------------------- */
public fun <C : ClassDefinition<HasTypeParameters>> C.typeParameters(parameters: List<TypeParameter>): C = apply {
    typeParameters.addAll(parameters)
}

public fun <C : ClassDefinition<HasTypeParameters>> C.typeParameters(vararg typeParameter: TypeParameter): C =
    typeParameters(typeParameter.asList())

public infix fun <C : ClassDefinition<HasTypeParameters>> C.typeParameter(param: TypeParameter): C =
    typeParameters(param)

public infix fun <C : ClassDefinition<HasTypeParameters>> C.typeParameter(name: String): C =
    typeParameter(invariant(name))

public fun <C : ClassDefinition<HasPrimaryConstructor>> C.primaryConstructor(
    modifiers: Modifiers,
    parameters: List<Parameter>
): C = apply {
    hasPrimaryConstructor = true
    constructorModifiers = modifiers.modifiers
    constructorParameters = parameters
}

public fun <C : ClassDefinition<HasPrimaryConstructor>> C.primaryConstructor(
    modifiers: Modifiers,
    vararg parameters: Parameter
): C = primaryConstructor(modifiers, parameters.asList())

public fun <C : ClassDefinition<HasPrimaryConstructor>> C.primaryConstructor(parameters: List<Parameter>): C =
    primaryConstructor(Modifiers(), parameters)

public fun <C : ClassDefinition<HasPrimaryConstructor>> C.primaryConstructor(vararg parameters: Parameter): C =
    primaryConstructor(parameters.asList())

public fun <C : ClassDefinition<CanImplement>> C.implements(type: Type, by: Expr? = null): C = apply {
    supertypes.add(Supertype(type, null, by))
}

public fun <C : ClassDefinition<CanImplement>> C.implements(raw: String, by: Expr? = null): C =
    implements(type(raw), by)

public fun <C : ClassDefinition<CanExtend>> C.extends(type: Type, arguments: List<Expr>?): C = apply {
    supertypes.add(Supertype(type, arguments, null))
}

public fun <C : ClassDefinition<CanExtend>> C.extends(raw: String, arguments: List<Expr>?): C =
    extends(type(raw), arguments)

public fun <C : ClassDefinition<CanExtend>> C.extends(type: Type, vararg arguments: Expr): C =
    extends(type, arguments.asList())

public fun <C : ClassDefinition<CanExtend>> C.extends(raw: String, vararg arguments: Expr): C =
    extends(raw, arguments.asList())

public inline infix fun <C : ClassDefinition<*>> C.body(block: ClassRobot.() -> Unit): C = apply {
    declarations = ClassRobot(imports).apply(block).declarations()
}

@JvmName("enumBody")
public inline infix fun ClassDefinition<DeclarationType.Enum>.body(
    block: EnumRobot.() -> Unit
): ClassDefinition<DeclarationType.Enum> = apply {
    val robot = EnumRobot(imports)
    robot.block()
    enumEntries = robot.enumEntries
    declarations = robot.declarations()
}
