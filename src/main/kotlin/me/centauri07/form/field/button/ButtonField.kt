package me.centauri07.form.field.button

import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType
import me.centauri07.form.field.exception.ValidatorException
import me.centauri07.form.field.FormField

/**
 * @author Centauri07
 */
class ButtonField(name: String, required: Boolean, buttons: List<ButtonChoice>.() -> Unit): FormField<ButtonField.ButtonChoice>(name, required) {
    override var value: ButtonChoice? = null

    private val buttons: List<Button>

    init {
        val buttonChoices = listOf<ButtonChoice>()
        buttons(buttonChoices)

        this.buttons = buttonChoices.map { Button(ButtonType.PRIMARY, "FBF-${it.id}", it.label) }

        if (this.buttons.isEmpty()) throw IllegalStateException("Buttons cannot be empty!")
    }

    override fun call(obj: Any): Result<FormField<*>>? {
        if (obj !is Button) return null

        if (!buttons.contains(obj)) return null

        val button = ButtonChoice(obj.id.drop(4), obj.label)

        validators.forEach {
            if (!it.validator(button)) return Result.failure(ValidatorException(it.message))
        }

        value = button

        return Result.success(this)
    }

    override fun inquire(): MessageRequest = MessageRequest(
        embeds = mutableListOf(Embed("$name - Please select one of the button below.")),
        buttons = this.buttons
    )

    data class ButtonChoice(val id: String, val label: String)
}