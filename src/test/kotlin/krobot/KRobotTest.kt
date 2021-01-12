/**
 *@author Nikolaus Knop
 */

package krobot

import krobot.api.*
import org.junit.jupiter.api.Test
import java.awt.Robot

class KRobotTest {
    @Test
    fun test() {
        val file = kotlinFile {
            `package`("krobot")
            `class`("ExampleClass", `in`("T"))
                .primaryConstructor(
                    `@`("PublishedApi").internal,
                    private.`val`.parameter("wrapped") of type("List", "Int")
                )
                .implements(type("List", "Int"), by = get("wrapped"))
                .implements(type("Complex", type("A"), type("B")))
                .extends("Any", emptyList()) body {
                inline.`fun`(
                    listOf(invariant("T") lowerBound "Any"),
                    "f",
                    "x" of "Int" default lit(3),
                    "l" of type("List", "Int"),
                    crossinline.parameter("block") of import<Robot>().functionType(
                        type("Int"),
                        returnType = type("Int")
                    )
                ) returnType "Int" body {
                    +call("println", get("x"))
                    +`if`(get("x") eq lit(3)).then {
                        +"println"(lit("default value supplied"))
                        +"println"(lit("test"))
                    }.`else` {
                        +"println"(lit("value of \$x supplied"))
                        +"println"(lit("test"))
                    }
                    +"require"(get("l").call("sum") + get("x") less lit(10), closure { +lit("error") })
                    +`when` {
                        get("x") eq lit(1) then {
                            +"println"(lit(1))
                        }
                        `else` {
                            +"println"(lit(2))
                        }
                    }
                    +`when`(get("x")) {
                        `is`("Int") then "println"(lit("is integer"))
                        `in`(`this`("ExampleClass")) then "println"(lit("is in collection"))
                        lit(3) then "println"(lit("is three"))
                        `else` {
                            +"println"(lit("hurray"))
                        }
                    }
                }
                private.constructor("test" of "Int").delegate("test".e * lit(2))
                private.`fun`("f")
                public.`class`("Inner")
            }
        }
        println(file.pretty())
    }
}