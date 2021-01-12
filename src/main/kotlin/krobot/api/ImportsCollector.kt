package krobot.api

import krobot.ast.Import

@PublishedApi internal class ImportsCollector(
    private val imported: MutableSet<String> = mutableSetOf(),
    private val dest: MutableList<Import> = mutableListOf()
) {
    fun finish(): List<Import> = dest

    fun add(import: Import) {
        if (imported.add(import.name)) dest.add(import)
    }

    fun addAll(imports: ImportsCollector) {
        for (import in imports.dest) add(import)
    }
}