package me.centauri07.form.field

/**
 * @author Centauri07
 */
interface NestableField<T: FormField<*>> {

    fun getUnacknowledgedField(): FormField<*>?
    fun add(formField: T): T

}