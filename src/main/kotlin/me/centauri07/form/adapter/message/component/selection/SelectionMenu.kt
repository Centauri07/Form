package me.centauri07.form.adapter.message.component.selection

/**
 * @author Centauri07
 */
data class SelectionMenu(
    val id: String,
    val placeholder: String,
    val options: List<SelectionOption>,
)

data class SelectionOption(
    val label: String,
    val value: String,
    val description: String? = null,
    val emoji: String? = null
)