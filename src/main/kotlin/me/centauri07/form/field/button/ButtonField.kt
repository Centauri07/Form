package me.centauri07.form.field.button

import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.field.exception.ValidatorException
import me.centauri07.form.field.FormField

/**
 * @author Centauri07
 */
class ButtonField(name: String, required: Boolean, buttons: List<Button>.() -> Unit): FormField<ButtonField.ButtonChoice>(name, required) {
    override var value: ButtonChoice? = null

    private val buttons: List<Button> = mutableListOf()

    init {
        buttons(this.buttons)

        if (this.buttons.isEmpty()) throw IllegalStateException("Buttons cannot be empty!")
    }

    override fun call(obj: Any): Result<FormField<*>>? {
        if (obj !is ButtonChoice) return null

        validators.forEach {
            if (!it.validator(obj)) return Result.failure(ValidatorException(it.message))
        }

        value = obj

        return Result.success(this)
    }

    override fun inquire(): MessageRequest = MessageRequest(
        embeds = mutableListOf(
            Embed() // TODO
        ),
        buttons = this.buttons
    )

    data class ButtonChoice(val id: String, val label: String)
}