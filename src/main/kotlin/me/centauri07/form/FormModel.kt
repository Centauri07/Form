package me.centauri07.form

import me.centauri07.form.field.FormField
import me.centauri07.form.field.group.GroupFormField

/**
 * @author Centauri07
 */
abstract class FormModel(name: String): GroupFormField<FormField<*>>() {

    abstract fun setup(form: Form)

    abstract fun onFinish(form: Form)
    abstract fun onExpire(form: Form)

}