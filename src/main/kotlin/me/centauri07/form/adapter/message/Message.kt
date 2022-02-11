package me.centauri07.form.adapter.message

import me.centauri07.form.adapter.message.component.button.Button
import me.centauri07.form.adapter.message.component.button.SelectionMenu
import java.awt.Color

/**
 * @author Centauri07
 */
data class Message(
    val id: Long,
    val channel: Channel,
    val authorId: Long,
    val memberId: Long?,
    val content: String,
    val mentions: List<Long>? = null,
    val mentionedRoles: List<Long>? = null,
    val mentionedChannels: List<Channel>? = null,
    val embeds: List<Embed>? = null,
    val buttons: List<Button>? = null,
    val selectionMenu: SelectionMenu?,
    val pinned: Boolean
)

data class MessageRequest(
    val content: String? = null,
    val embeds: List<Embed>? = null,
    val buttons: List<Button>? = null,
    val selectionMenu: SelectionMenu? = null
)

data class Channel(
    val id: Long,
    val guildId: Long? = null,
    val name: String
)

data class Embed(
    val title: String? = null,
    val description: String? = null,
    val color: Color? = null,
    val footer: String? = null,
    val authorId: Long? = null,
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