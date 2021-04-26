/**
 *@author Nikolaus Knop
 */

package krobot

import krobot.api.*
import org.junit.jupiter.api.Test

class KRobotTest {
    @Test
    fun sample1() {
        val f = kotlinFile {
            `package`("com.example")
            internal.`object`("Main").body {
                `@`("JvmStatic").`fun`("main", "args" of "Array<String>").body {
                    `val`("msg") initializedWith `when`("args".e.select("size")) {
                        lit(0) then lit("This is not possible...")
                        lit(1) then lit("Oh no, you provided no arguments")
                        `in`(lit(2)..lit(5)) then lit("The number of arguments is ok")
                        `else` then lit("Too many arguments")
                    }
                }
            }
        }
        println(f.pretty())
    }

    @Test
    fun sample2() {
        val file = kotlinFile {
            import("kotlin.random.Random")
            `package`("foo.bar")
            abstract.`class`("ExampleClass", `in`("T"))
                .primaryConstructor(
                    `@`("PublishedApi").internal,
                    private.`val`.parameter("wrapped") of type("List", "Int")
                )
                .implements(type("List", "Int"), by = get("wrapped"))
                .extends("Any", emptyList()) body {
                inline.`fun`(
                    listOf(invariant("T") lowerBound "Any"),
                    "f",
                    "x" of "Int" default lit(3),
                    "l" of type("List", "Int"),
                    crossinline.parameter("block") of import<java.awt.Robot>().functionType(
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
                private.constructor("test" of "Int").delegate("listOf"("test".e, "Random".e.call("nextInt")))
                abstract.`fun`("f") returnType "Int"
                public.`class`("Inner")
            }
        }
        println(file.pretty())
    }
}
