package me.centauri07.form

import me.centauri07.form.adapter.channel.MessageChannelAdapter
import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageAdapter
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType
import me.centauri07.form.field.FormField
import me.centauri07.form.util.Message
import java.util.concurrent.TimeUnit

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

    fun call(message: MessageAdapter? = null, obj: Any? = null) {
        if (idle) return

        try {
            message?.delete()
        } catch (e: Exception) {
            // IGNORE
        }

        var field: FormField<*> = model.getUnacknowledgedField() ?: run { finish();return }

        obj?.let {
            field.call(obj)?.let {
                if (it.isSuccess) {
                    field.acknowledged = true
                    field.acknowledge?.invoke(this, field)

                    field = model.getUnacknowledgedField() ?: run { finish(); return }
                } else {
                    idle = true

                    Message.sendOrEdit(message, channel, MessageRequest(
                        embeds = mutableListOf(Embed("You cannot do that!", it.exceptionOrNull()?.message))
                    )).editAfter(3, TimeUnit.SECONDS, field.inquire()).let { messageAdapter ->
                        this.message = messageAdapter
                    }

                    return
                }
            } ?: return
        }

        if (!field.required && !field.chosen) {
            idle = true

            this.message = Message.sendOrEdit(
                message, channel, MessageRequest(
                    embeds = mutableListOf(Embed("Do you want to enter ${field.name}?")),
                    buttons = mutableListOf(field.yesButton, field.noButton)
                )
            )

            return
        }

        this.message = Message.sendOrEdit(message, channel, field.inquire())
    }

    fun finish() {
        if (idle) return

        if (!confirmOnFinish) {
            model.onFinish(this)
            FormManager.removeForm(userId)
        } else {
            idle = true
            FormManager.setAcknowledge(userId)

            Message.sendOrEdit(
                message, channel, MessageRequest(
                    embeds = mutableListOf(
                        Embed("Session Finished!",
                            "Please choose one of the button below whether you want to submit the form or not.")
                    ),
                    buttons = mutableListOf(submitButton, cancelButton)
                )
            )
        }
    }

    fun expire() {
        Message.sendOrEdit(message, channel, MessageRequest(
                embeds = mutableListOf(
                    Embed("Session has been canceled.",
                        "You have been inactive for 3 minutes, we're now cancelling this session.")
                )
            )
        )

        model.onExpire(this)
    }
}