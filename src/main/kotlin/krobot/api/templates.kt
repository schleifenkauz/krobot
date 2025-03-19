package krobot.api

import krobot.templates.Template
import krobot.ast.TemplateElement
import krobot.ast.UniversalElement


fun Template.format(vararg arguments: Any?): UniversalElement = TemplateElement(this, arguments.asList())

fun String.format(vararg arguments: Any?): UniversalElement = Template.parse(this).format(*arguments)