package me.centauri07.form.field.input.reader

import me.centauri07.form.field.FormField
import me.centauri07.form.field.exception.ValidatorException

/**
 * @author Centauri07
 */
abstract class InputReader<T>(val tClass: Class<T>) {

    abstract val converter: (String) -> T?

    fun read(input: String, validator: List<FormField.Validator<T>>): Result<T> {
        return try {
            val result = converter(input) ?: return Result.failure(IllegalArgumentException("The input that you've entered doesn't follow the required format."))

            validator.forEach {
                if (!it.validator(result)) return Result.failure(ValidatorException(it.message))
            }

            Result.success(result)
        } catch (throwable: Throwable) {
            Result.failure(Throwable("The input that you've entered doesn't follow the required format.", throwable))
        }
    }
}

object InputReaderRegistry {
    private val readers = mutableMapOf<Class<*>, InputReader<*>>()

    init {
        add(StringReader())
        add(ColorReader())

        add(ByteReader())
        add(ShortReader())
        add(IntReader())
        add(LongReader())
        add(FloatReader())
        add(DoubleReader())

        add(BooleanReader())
        add(CharReader())

        add(URLReader())
    }

    fun add(reader: InputReader<*>) = readers.put(reader.tClass, reader)
    fun remove(clazz: Class<*>) = readers.remove(clazz)

    fun has(clazz: Class<*>) = get(clazz) != null
    fun <T> get(clazz: Class<T>): InputReader<T>? = readers[clazz] as InputReader<T>?
}