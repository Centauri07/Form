package me.centauri07.form.adapter.channel

import me.centauri07.form.adapter.message.Channel
import me.centauri07.form.adapter.message.MessageAdapter
import me.centauri07.form.adapter.message.MessageRequest
import java.util.concurrent.TimeUnit

/**
 * @author Centauri07
 */
interface MessageChannelAdapter {
    val channel: Channel

    fun sendMessage(message: MessageRequest): MessageAdapter

    fun editMessageById(id: Long, replacement: MessageRequest): MessageAdapter?

    fun deleteMessageById(id: Long)

    fun getMessageById(id: Long): MessageAdapter
}