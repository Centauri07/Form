package me.centauri07.form.field.selection

import me.centauri07.form.FormManager
import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType
import me.centauri07.form.adapter.message.component.selection.SelectionMenu
import me.centauri07.form.adapter.message.component.selection.SelectionOption
import me.centauri07.form.field.option.OptionField

/**
 * @author Centauri07
 */
class SelectionField(name: String, description: String? = null, required: Boolean, options: MutableList<SelectionOption>.() -> Unit): OptionField<SelectionOption>(name, description, required, options) {
    override fun inquire(): MessageRequest = MessageRequest(
        embeds = mutableListOf(Embed("${this.name} - Select one of the options.", description, FormManager.defaultColor)),
        selectionMenu = SelectionMenu("SF-${this.name}", name.toString(), options)
    )
}