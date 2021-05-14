/**
 * @author Nikolaus Knop
 */

package krobot.ast

import krobot.impl.IndentedWriter

public sealed interface Element {
    public fun append(out: IndentedWriter)

    public fun pretty(): String {
        val out = StringBuilder()
        append(IndentedWriter(out))
        return out.toString()
    }
}