package me.centauri07.form.adapter.message

import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.selection.SelectionMenu
import java.awt.Color

/**
 * @author Centauri07
 */
data class Message(
    val id: Long,
    val channel: Channel,
    val authorId: Long,
    val content: String
)

data class MessageRequest(
    val content: String? = null,
    val embeds: List<Embed>? = null,
    val buttons: List<Button>? = null,
    val selectionMenu: SelectionMenu? = null
)

data class Channel(
    val id: Long,
    val name: String
)

data class Embed(
    val title: String? = null,
    val description: String? = null,
    val color: Color? = null,
    val footer: Footer? = null,
    val authorUrl: String? = null,
    val fields: List<Field>? = null
)

data class Footer(
    val text: String,
    val iconUrl: String? = null
)

data class Field(
    val name: String,
    val value: String,
    val inline: Boolean
)