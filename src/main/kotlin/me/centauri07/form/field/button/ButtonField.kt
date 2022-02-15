package me.centauri07.form.field.button

import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType
import me.centauri07.form.field.option.OptionField

/**
 * @author Centauri07
 */
class ButtonField(name: String, required: Boolean, buttons: List<Button>.() -> Unit): OptionField<Button>(name, required, options = buttons) {
    override fun inquire(): MessageRequest = MessageRequest(
        embeds = mutableListOf(Embed("$name - Please select one of the button below.")),
        buttons = this.options.map { Button(ButtonType.SECONDARY, it.id, it.label, emoji = it.emoji) }
    )
}