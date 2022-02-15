package me.centauri07.form.field.option

import me.centauri07.form.field.FormField
import me.centauri07.form.field.exception.ValidatorException
import java.lang.ClassCastException

/**
 * @author Centauri07
 */
abstract class OptionField<T>(name: String? = null, required: Boolean = true, options: List<T>.() -> Unit): FormField<T>(name, required) {
    override var value: T? = null

    protected val options: List<T>

    init {
        val optionChoices = listOf<T>()
        options(optionChoices)

        this.options = optionChoices

        if (this.options.isEmpty()) throw IllegalStateException("Options cannot be empty!")
    }

    override fun call(obj: Any): Result<FormField<*>>? {
        try {
            obj as T
        } catch (e: ClassCastException) {
            return null
        }

        if (!options.contains(obj)) return null

        validators.forEach {
            if (!it.validator(obj)) return Result.failure(ValidatorException(it.message))
        }

        value = obj

        return Result.success(this)
    }

}