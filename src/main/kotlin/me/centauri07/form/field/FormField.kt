package me.centauri07.form.field

import me.centauri07.form.Form
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType

/**
 * @author Centauri07
 */
abstract class FormField<T>(val name: String? = null, val required: Boolean = true) {
    abstract var value: T?

    var validators: MutableList<Validator<T>> = mutableListOf()
    var acknowledge: (Form.(FormField<*>) -> Unit)? = null

    var chosen: Boolean = required
    var acknowledged: Boolean = false

    var yesButton: Button = Button(ButtonType.SUCCESS, "form-field_yes-button", "✅ Yes")
    var noButton: Button = Button(ButtonType.DANGER, "form-field_no-button", "❌ No")

    abstract fun call(obj: Any): Result<FormField<*>>?

    abstract fun inquire(): MessageRequest

    fun validate(validator: T.() -> Boolean, errMessage: String) = validators.add(Validator(validator, errMessage))

    class Validator<T>(val validator: T.() -> Boolean, val message: String)
}