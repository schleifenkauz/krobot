package krobot.api

import krobot.ast.Import

internal class ImportsCollector(
    private val imported: MutableSet<String> = mutableSetOf(),
    private val dest: MutableList<Import> = mutableListOf()
) {
    fun finish(): List<Import> = dest

    fun add(import: Import) {
        if (imported.add(import.name)) dest.add(import)
    }
}