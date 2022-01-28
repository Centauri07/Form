package me.centauri07.form.adapter.message.component.button

/**
 * @author Centauri07
 */
data class SelectionMenu(
    val id: String,
    val placeholder: String,
    val options: MutableList<SelectionOption>
)

data class SelectionOption(
    val label: String,
    val value: String,
    val description: String,
    val emoji: String
)