/**
 * @author Nikolaus Knop
 */

package krobot.ast

public sealed class Element {
    internal abstract fun append(out: IndentedWriter)

    internal fun pretty(): String {
        val out = StringBuilder()
        append(IndentedWriter(out))
        return out.toString()
    }
}