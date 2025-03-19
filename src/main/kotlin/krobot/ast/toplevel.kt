/**
 * @author Nikolaus Knop
 */

package krobot.ast

import krobot.impl.IndentedWriter
import java.io.Closeable
import java.io.File
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path

data class Import internal constructor(
    internal val name: Identifier,
    internal var alias: String? = null
) : Element {
    override fun append(out: IndentedWriter): Unit = with(out) {
        append("import ")
        append(name)
        join(" as ", alias)
    }
}

abstract class TopLevelElement internal constructor() : Element {
    internal abstract val packageName: String?
    internal abstract val defaultExtension: String

    fun <A> saveTo(output: A) where A : Appendable, A : Closeable {
        val writer = IndentedWriter(output)
        append(writer)
        output.close()
    }

    fun saveTo(output: OutputStream) {
        saveTo(output.bufferedWriter())
    }

    fun saveTo(file: File) {
        saveTo(file.bufferedWriter())
    }

    fun saveTo(path: Path) {
        saveTo(Files.newBufferedWriter(path))
    }

    fun saveToSourceRoot(sourceRoot: File, fileName: String) {
        val dir = packageName.orEmpty().split(".").fold(sourceRoot, File::resolve)
        dir.mkdirs()
        val file = dir.resolve(fileName.addExtension())
        saveTo(file)
    }

    private fun String.addExtension() = if (contains('.')) this else "$this$defaultExtension"

    fun saveToSourceRoot(sourceRoot: Path, fileName: String) {
        saveToSourceRoot(sourceRoot.toFile(), fileName)
    }

    fun saveToSourceRoot(sourceRoot: String, fileName: String) {
        saveToSourceRoot(File(sourceRoot), fileName)
    }
}

data class KotlinFile @PublishedApi internal constructor(
    override val packageName: String?,
    private val imports: List<Import>,
    internal val declarations: List<Declaration>
) : TopLevelElement() {
    override val defaultExtension: String
        get() = ".kt"

    override fun append(out: IndentedWriter): Unit = with(out) {
        join("package ", packageName)
        join(imports, NewLine, prefix = NewLine)
        join(declarations, NewLine, prefix = NewLine)
    }
}

data class KotlinScript @PublishedApi internal constructor(
    override val packageName: String?,
    private val imports: List<Import>,
    private val elements: List<BlockElement>
) : TopLevelElement() {
    override val defaultExtension: String
        get() = ".kts"

    override fun append(out: IndentedWriter): Unit = with(out) {
        join("package ", packageName)
        join(imports, NewLine, postfix = NewLine)
        join(elements, NewLine)
    }
}

class NamedTopLevelElement(private val name: String, private val wrapped: TopLevelElement) : TopLevelElement() {
    override val packageName: String?
        get() = wrapped.packageName

    override val defaultExtension: String
        get() = wrapped.defaultExtension

    override fun append(out: IndentedWriter) {
        wrapped.append(out)
    }

    fun saveToSourceRoot(sourceRoot: File) {
        saveToSourceRoot(sourceRoot, name)
    }

    fun saveToSourceRoot(sourceRoot: Path) {
        saveToSourceRoot(sourceRoot, name)
    }

    fun saveToSourceRoot(sourceRoot: String) {
        saveToSourceRoot(sourceRoot, name)
    }
}