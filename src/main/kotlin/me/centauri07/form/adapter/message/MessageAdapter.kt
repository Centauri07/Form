package me.centauri07.form.adapter.message

import java.util.concurrent.TimeUnit

/**
 * @author Centauri07
 */
interface MessageAdapter {
    val message: Message
    
    fun edit(replacement: MessageRequest): MessageAdapter
    fun delete()

    fun editAfter(amount: Int, unit: TimeUnit, messageRequest: MessageRequest): MessageAdapter
    fun deleteAfter(amount: Int, unit: TimeUnit)
}