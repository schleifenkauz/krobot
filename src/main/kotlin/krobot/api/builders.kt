/**
 * @author Nikolaus Knop
 */

package krobot.api

/* ---------------------------------------
Utility functions
  --------------------------------------- */
@PublishedApi internal inline fun makeBody(
    imports: ImportsCollector,
    block: BlockRobot.() -> Unit
) = BlockRobot(imports).apply(block).finish()



