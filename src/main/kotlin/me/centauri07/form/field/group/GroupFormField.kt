package me.centauri07.form.field.group

import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.field.FormField
import me.centauri07.form.field.NestableField

/**
 * @author Centauri07
 */
open class GroupFormField<T: FormField<*>>(
    name: String? = null,
    required: Boolean = true,
    fields: (MutableList<T>.() -> Unit)? = null
): FormField<MutableList<T>>(name, required), NestableField<T> {

    final override var value: MutableList<T>? = null

    init {
        mutableListOf<T>().let { list ->
            fields?.let {
                it.invoke(list)

                value = list
            }
        }
    }

    override fun call(obj: Any): Result<FormField<*>>? = getUnacknowledgedField()?.call(obj)

    override fun inquire(): MessageRequest {
        return getUnacknowledgedField()?.inquire() ?: MessageRequest(
            embeds = mutableListOf(
                Embed("An error occurred when executing this command.", "Please contact a staff or an administrator.")
            )
        )
    }

    override fun getUnacknowledgedField(): FormField<*>? {
        var currentField: FormField<*>? = value?.find { !it.acknowledged }

        while (currentField != null && currentField is NestableField<*> && currentField.chosen) {
            val field = currentField.getUnacknowledgedField()

            if (field == null) {

                currentField.acknowledged = true
                currentField = value?.find { !it.acknowledged }

            } else currentField = field

        }

        return currentField
    }

    override fun add(formField: T): T {
        if (value == null) value = mutableListOf()

        value?.add(formField)

        return formField
    }
}