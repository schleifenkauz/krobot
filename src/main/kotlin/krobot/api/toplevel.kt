/**
 * @author Nikolaus Knop
 */

package krobot.api

import krobot.ast.KotlinFile
import krobot.ast.KotlinScript

public inline fun kotlinFile(block: KotlinFileRobot.() -> Unit): KotlinFile =
    KotlinFileRobot().apply(block).finishFile()

public inline fun kotlinScript(block: BlockRobot.() -> Unit): KotlinScript =
    KotlinScriptRobot().apply(block).finishScript()