package me.centauri07.form

import me.centauri07.form.field.FormField
import me.centauri07.form.field.group.GroupFormField

/**
 * @author Centauri07
 */
abstract class FormModel(name: String, description: String? = null, val submitOnFinish: Boolean): GroupFormField<FormField<*>>(name, description, true) {

    abstract fun setup(form: Form)

    abstract fun onFinish(form: Form)

}