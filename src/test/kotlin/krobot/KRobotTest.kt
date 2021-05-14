/**
 *@author Nikolaus Knop
 */

package krobot

import krobot.api.*
import krobot.ast.Expr
import krobot.templates.Template
import org.junit.jupiter.api.Test

class KRobotTest {
    @Test
    fun sample1() {
        val f = kotlinFile {
            `package`("com.example")
            +internal.`object`("Main").body {
                +`@`("JvmStatic").`fun`("main", "args" of "Array<String>").body {
                    +`val`("msg") initializedWith `when`("args".e.select("size")) {
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
            +abstract.`class`("ExampleClass", `in`("T"))
                .primaryConstructor(
                    `@`("PublishedApi").internal,
                    private.`val`.parameter("wrapped") of type("List", "Int")
                )
                .implements(type("List", "Int"), by = get("wrapped"))
                .extends("Any", emptyList()) body {
                +inline.`fun`(
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
                +private.constructor("test" of "Int").delegate("listOf"("test".e, "Random".e.call("nextInt")))
                +abstract.`fun`("f") returnType "Int"
                +public.`class`("Inner")
                +internal.enum("E").primaryConstructor(`val`.parameter("x") of "Int".t).body {
                    +abstract.`fun`("f") returnType "Int"
                    +"X"("1".e) {
                        +override.`fun`("f") returnType "Int" returns "1".e
                    }
                }
            }
        }
        println(file.pretty())
    }

    @Test
    fun sample3() {
        val f = kotlinScript {
            write {
                append("//hello world")
            }
            +multilineComment(
                "a",
                "b",
                "c"
            )
            +"val x = 1"
            +"val @1 = @2".format("y", lit(2) + lit(3))
            +`fun`("f", "vararg xs" of "Int") returnType "Int" returns "xs".e.call("asList").call("sum()")
            +"val f = 0"
            val template = Template.parse("val @1 = f{(*2{, })}")
            +template.format("a", listOf(lit(1), lit(2), lit(3)))
            +template.format("b", emptyList<Expr>())
        }
        println(f.pretty())
    }

    @Test
    fun sample4() {
        println("val @1 = 1".format("x").pretty())
        println("val x = listOf(*1{, })".format(listOf(lit(1), lit(2), lit(3))).pretty())
        println("{val x = @1}".format(null).pretty())
        println("val x = @1{ + @2}".format(lit(1), lit(2)).pretty())
        println("1 + 2 + 3{ + *1{ + }}".format(listOf(lit(4), lit(5), null)).pretty())
        println("(@1..@2).forEach \\{ println(it \\* it) \\}".format(lit(1), lit(5)).pretty())
        println("listOf(*1{, }[2.@2 \\* @2])".format(listOf("1", "2", "3")).pretty())
        println("val x = @2{ + {@1 \\* @2} + {@1 \\* @3}}".format(lit(1), lit(2), null).pretty())
    }
}