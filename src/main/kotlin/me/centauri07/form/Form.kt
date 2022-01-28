package me.centauri07.form

import me.centauri07.form.adapter.channel.MessageChannelAdapter
import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageAdapter
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType
import me.centauri07.form.field.FormField

/**
 * @author Centauri07
 */
class Form(val model: FormModel, val userId: Long, val channel: MessageChannelAdapter, val confirmOnFinish: Boolean) {
    var idle: Boolean = false

    var submitButton: Button = Button(ButtonType.SUCCESS, "form-submit", "✅ Submit")
    var cancelButton: Button = Button(ButtonType.DANGER, "form-cancel", "❌ Cancel")

    var message: MessageAdapter? = null

    init {
        model.setup(this)
    }

    fun call(obj: Any? = null) {
        if (idle) return

        var field: FormField<*> = model.getUnacknowledgedField() ?: run { finish();return }

        obj?.let {
            field.call(obj)?.let {
                if (it.isSuccess) {
                    field.acknowledged = true
                    field.acknowledge?.invoke(this, field)

                    field = model.getUnacknowledgedField() ?: run { finish(); return }
                } else {
                    message = channel.sendMessage(
                        MessageRequest(
                            embeds = mutableListOf(
                                Embed(
                                    "You cannot do that!",
                                    it.exceptionOrNull()?.message
                                )
                            )
                        )
                    )

                    return
                }
            }
        } ?: run {
            channel.sendMessage(
                MessageRequest(
                    embeds = mutableListOf(
                        Embed(
                            "An error occurred when executing this command.",
                            "Please contact a staff or an administrator."
                        )
                    )
                )
            )
        }

        if (!field.required && !field.chosen) {
            idle = true

            message = channel.sendMessage(
                MessageRequest(
                    embeds = mutableListOf(
                        Embed(
                            "Do you want to enter ${field.name}?",
                        )
                    ),
                    buttons = mutableListOf(field.yesButton, field.noButton)
                )
            )

            return
        }

        message = channel.sendMessage(field.inquire())
    }

    fun finish() {
        if (idle) return

        if (!confirmOnFinish) {
            model.onFinish(this)
            // TODO cache thingy
        } else {
            idle = true
            // TODO cache thingy

            channel.sendMessage(
                MessageRequest(
                    embeds = mutableListOf(
                        Embed(
                            "Session Finished!",
                            "Please choose one of the button below whether you want to submit the form or not."
                        )
                    ),
                    buttons = mutableListOf(
                        submitButton, cancelButton
                    )
                )
            )
        }
    }

    fun expire() {
        channel.sendMessage(
            MessageRequest(
                embeds = mutableListOf(
                    Embed(
                        "Session has been canceled.",
                        "You have been inactive for 3 minutes, we're now cancelling this session."
                    )
                )
            )
        )

        model.onExpire(this)
    }
}