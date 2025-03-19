/**
 * @author Nikolaus Knop
 */

package krobot.ast

import krobot.impl.IndentedWriter

sealed interface Element {
    fun append(out: IndentedWriter)

    fun pretty(): String {
        val out = StringBuilder()
        append(IndentedWriter(out))
        return out.toString()
    }
}