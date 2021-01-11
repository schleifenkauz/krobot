/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.ast.Import
import krobot.ast.Type
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import javax.lang.model.element.TypeElement
import javax.lang.model.type.*
import kotlin.reflect.*
import kotlin.reflect.jvm.jvmErasure

@KotlinRobotDsl
public abstract class KotlinRobot internal constructor(
    @PublishedApi internal val imports: ImportsCollector
) : Modifiers() {
    public fun import(className: String): Import {
        val import = Import(className)
        imports.add(import)
        return import
    }

    public fun import(element: TypeElement): Type {
        import(element.toString())
        return type(element.simpleName.toString())
    }

    @OptIn(ExperimentalStdlibApi::class)
    public inline fun <reified T : Any> import(): Type = import(typeOf<T>())

    public fun import(cls: KClass<*>): Type {
        val fqName = cls.qualifiedName ?: error("illegal anonymous type")
        import(fqName)
        return type(cls.simpleName!!)
    }

    public fun import(clazz: Class<*>): Type {
        val fqName = clazz.name
        import(fqName)
        return type(clazz.simpleName)
    }

    public fun import(type: java.lang.reflect.Type): Type = when (type) {
        is Class<*>                          -> import(type)
        is ParameterizedType                 -> {
            val raw = type.rawType as Class<*>
            import(raw)
            type(raw.simpleName, type.actualTypeArguments.map { import(it) })
        }
        is java.lang.reflect.TypeVariable<*> -> type(type.name)
        is GenericArrayType                  -> type("Array", import(type.genericComponentType))
        is WildcardType                      -> error("Wildcard types like $type are not supported")
        else                                 -> error("Unknown type $type")
    }

    public fun import(type: KType): Type {
        import(type.jvmErasure)
        return type(type.jvmErasure.simpleName!!, type.arguments.map { t -> t.type?.let { import(it) } ?: star })
    }

    public fun import(type: TypeMirror): Type = when (type) {
        is DeclaredType   -> {
            val element = type.asElement() as TypeElement
            import(element.qualifiedName.toString())
            type(element.simpleName.toString())
        }
        is ArrayType      -> type("Array", import(type.componentType))
        is ExecutableType -> functionType(type.parameterTypes.map { import(it) }, import(type.returnType))
        is TypeVariable   -> type(type.asElement().simpleName.toString())
        is WildcardType   -> error("Wildcard types like $type are not supported")
        is PrimitiveType  -> error("Primitive types like $type are not supported")
        else              -> error("Unknown type kind: $type")
    }
}