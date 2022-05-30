package me.centauri07.form.field.button

import me.centauri07.form.FormManager
import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType
import me.centauri07.form.field.option.OptionField

/**
 * @author Centauri07
 */
class ButtonField(name: String, description: String? = null, required: Boolean, buttons: MutableList<Button>.() -> Unit): OptionField<Button>(name, description, required, options = buttons) {
    override fun inquire(): MessageRequest = MessageRequest(
        embeds = mutableListOf(Embed(name, description, FormManager.defaultColor)),
        buttons = this.options.map { Button(ButtonType.SECONDARY, it.id, it.label, emoji = it.emoji) }
    )
}