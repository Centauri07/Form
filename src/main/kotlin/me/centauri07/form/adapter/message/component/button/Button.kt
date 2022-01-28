package me.centauri07.form.adapter.message.component.button

/**
 * @author Centauri07
 */
data class Button(
    val type: ButtonType,
    val id: String,
    val label: String,
    val enabled: Boolean = true,
    val emoji: String? = null
)

enum class ButtonType {
    DANGER,
    LINK,
    PRIMARY,
    SECONDARY,
    SUCCESS,
    UNKNOWN
}