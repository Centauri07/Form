package me.centauri07.form

import me.centauri07.form.adapter.channel.MessageChannelAdapter
import me.centauri07.form.adapter.message.Embed
import me.centauri07.form.adapter.message.MessageAdapter
import me.centauri07.form.adapter.message.MessageRequest
import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.ButtonType
import me.centauri07.form.field.FormField
import java.util.concurrent.TimeUnit

/**
 * @author Centauri07
 */
class Form(val model: FormModel, val userId: Long, val channel: MessageChannelAdapter) {
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

        var field: FormField<*> = model.getUnacknowledgedField() ?: run { finish(); return }

        obj?.let {
            field.call(obj)?.let {
                if (it.isSuccess) {
                    field.acknowledged = true
                    field.acknowledge?.invoke(this, field)

                    field = model.getUnacknowledgedField() ?: run { finish(); return }
                } else {
                    idle = true

                    sendOrEdit(message, channel, MessageRequest(
                        embeds = mutableListOf(Embed("You cannot do that!", it.exceptionOrNull()?.message))
                    )).editAfter(3, TimeUnit.SECONDS, field.inquire()).let { run {
                        idle = false
                        this.message = it
                        call()
                    } }

                    return
                }
            } ?: return
        }

        if (!field.required && !field.chosen) {
            idle = true

            sendOrEdit(
                message, channel, MessageRequest(
                    embeds = mutableListOf(Embed("Do you want to enter ${field.name}?")),
                    buttons = mutableListOf(field.yesButton, field.noButton)
                )
            )

            return
        }

        sendOrEdit(message, channel, field.inquire())
    }

    fun finish() {
        if (idle) return

        if (!model.submitOnFinish) {
            model.onFinish(this)
            FormManager.removeForm(userId)
        } else {
            idle = true
            FormManager.setAcknowledge(userId)

            sendOrEdit(
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

    fun cancel(reason: String?) {
        sendOrEdit(message, channel, MessageRequest(embeds = mutableListOf(Embed("Session has been canceled.", reason))))

        FormManager.removeForm(userId)
    }

    fun sendOrEdit(message: MessageAdapter?, channelAdapter: MessageChannelAdapter, messageRequest: MessageRequest): MessageAdapter {
        return try {
            message?.edit(messageRequest) ?: channelAdapter.sendMessage(messageRequest)
        } catch (e: Exception) {
            channelAdapter.sendMessage(messageRequest)
        }
    }
}