package me.centauri07.form.field.input

import me.centauri07.form.adapter.message.Message
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.field.FormField
import me.centauri07.form.field.input.reader.InputReader
import me.centauri07.form.field.input.reader.InputReaderRegistry
import java.lang.IllegalArgumentException

/**
 * @author Centauri07
 */
class InputField<T>(tClass: Class<T>, name: String, required: Boolean): FormField<T>(name, required) {
    private val reader: InputReader<T>? = InputReaderRegistry.get(tClass)

    init {
        if (reader == null) throw IllegalArgumentException("There is no reader found for ${tClass.name}")
    }

    override var value: T? = null

    override fun call(obj: Any): Result<FormField<T>>? {
        if (obj !is String) return null

        val result = reader?.read(obj, validators)

        return if (result?.isSuccess == true) {
            value = result.getOrNull()
            Result.success(this)
        } else {
            result?.exceptionOrNull()?.let { Result.failure(it) }
        }
    }

    override fun inquire(): MessageRequest = MessageRequest("Enter $name")
}