package org.service.cosmo.common.form.field

import me.centauri07.form.field.FormField

/**
 * @author Centauri07
 */
interface NestableField<T: FormField<*>> {

    fun getUnacknowledgedField(): FormField<*>?
    fun add(formField: T): T

}