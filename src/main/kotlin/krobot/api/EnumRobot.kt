package krobot.api

import krobot.ast.EnumEntry
import krobot.ast.Expr

public class EnumRobot @PublishedApi internal constructor(imports: ImportsCollector) : ClassRobot(imports) {
    @PublishedApi internal val enumEntries: MutableList<EnumEntry> = mutableListOf()

    public inline operator fun String.invoke(
        arguments: List<Expr> = emptyList(),
        block: AdvancedDeclarationsRobot.() -> Unit = {}
    ): EnumEntry {
        val robot = AdvancedDeclarationsRobot(imports, mutableListOf())
        val declarations = robot.apply(block).declarations()
        return EnumEntry(this, arguments, declarations)
    }

    public inline operator fun String.invoke(
        vararg arguments: Expr,
        block: AdvancedDeclarationsRobot.() -> Unit = {}
    ): EnumEntry = invoke(arguments.asList(), block)

    public operator fun EnumEntry.unaryPlus() {
        enumEntries.add(this)
    }
}