package krobot.api

import krobot.ast.*
import krobot.ast.PropertyInitializer
import krobot.ast.PropertyInitializer.Delegated
import krobot.ast.PropertyInitializer.Value

/* ---------------------------------------
Properties
  --------------------------------------- */
public infix fun <P : BasicProperty> P.of(type: Type): P = apply {
    this.type = type
}

public infix fun <P : BasicProperty> P.of(raw: String): P = of(type(raw))
public infix fun <P : BasicProperty> P.initializedWith(value: Expr): P = apply {
    initializer = Value(value)
}

public infix fun <P : BasicProperty> P.by(delegate: Expr) {
    initializer = Delegated(delegate)
}

public infix fun AdvancedProperty.receiver(type: Type): AdvancedProperty = apply {
    receiver = type
}

public infix fun AdvancedProperty.receiver(raw: String): AdvancedProperty = receiver(type(raw))
public inline infix fun AdvancedProperty.accessors(block: PropertyAccessorsRobot.() -> Unit) {
    PropertyAccessorsRobot(this).block()
}