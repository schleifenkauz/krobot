/**
 * @author Nikolaus Knop
 */

package krobot.ast

import krobot.ast.IndentedWriter.NewLine
import java.io.File
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path

public data class Import internal constructor(
    internal val name: Identifier,
    internal var alias: String? = null
) : Element() {
    override fun append(out: IndentedWriter) = with(out) {
        append("import ")
        append(name)
        join(" as ", alias)
    }
}

public abstract class TopLevelElement internal constructor() : Element() {
    internal abstract val packageName: String?

    public fun saveTo(output: Appendable) {
        val writer = IndentedWriter(output)
        writer.append(this)
    }

    public fun saveTo(output: OutputStream) {
        saveTo(output.bufferedWriter())
    }

    public fun saveTo(file: File) {
        saveTo(file.bufferedWriter())
    }

    public fun saveTo(path: Path) {
        saveTo(Files.newBufferedWriter(path))
    }

    public fun saveTo(sourceRoot: File, fileName: String) {
        val full = packageName.orEmpty().split(".").fold(sourceRoot, File::resolve).resolve(fileName)
        saveTo(full)
    }

    public fun saveTo(sourceRoot: Path, fileName: String) {
        val full = packageName.orEmpty().split(".").fold(sourceRoot, Path::resolve).resolve(fileName)
        saveTo(full)
    }
}

public data class KotlinFile @PublishedApi internal constructor(
    override val packageName: String?,
    private val imports: List<Import>,
    internal val declarations: List<Declaration>
) : TopLevelElement() {
    override fun append(out: IndentedWriter) = with(out) {
        join("package ", packageName)
        join(imports, NewLine, prefix = NewLine)
        join(declarations, NewLine, prefix = NewLine)
    }
}

public data class KotlinScript @PublishedApi internal constructor(
    override val packageName: String?,
    private val imports: List<Import>,
    private val elements: List<BlockElement>
) : TopLevelElement() {
    override fun append(out: IndentedWriter) = with(out) {
        join("package ", packageName)
        join(imports, NewLine, postfix = NewLine)
        join(elements, NewLine)
    }
}