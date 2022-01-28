package me.centauri07.form.adapter.user

data class UserData(
    val id: Long,
    val username: String,
    val discriminator: String,
    val avatar: String?,
    val bot: Boolean,
    val banner: String? = null,
    val accentColor: Int? = null
)
