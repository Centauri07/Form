package me.centauri07.form.adapter.message

/**
 * @author Centauri07
 */
interface MessageAdapter {
    val message: Message
    
    fun edit(replacement: MessageRequest)

    fun delete()
}