package me.centauri07.form.util

import me.centauri07.form.adapter.channel.MessageChannelAdapter
import me.centauri07.form.adapter.message.MessageAdapter
import me.centauri07.form.adapter.message.MessageRequest

object Message {

    fun sendOrEdit(message: MessageAdapter?, channelAdapter: MessageChannelAdapter, messageRequest: MessageRequest): MessageAdapter {
        return try {
            message?.edit(messageRequest) ?: channelAdapter.sendMessage(messageRequest)
        } catch (e: Exception) {
            channelAdapter.sendMessage(messageRequest)
        }
    }

}