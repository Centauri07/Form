package me.centauri07.form

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalCause
import me.centauri07.form.adapter.channel.MessageChannelAdapter
import java.awt.Color
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object FormManager {

    lateinit var defaultColor: Color
    lateinit var errorColor: Color

    private val forms: MutableMap<Long, Form> = mutableMapOf()

    init {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                for (form in forms) {

                    if (form.value.model !is Expireable) continue
                    if (form.value.lastBump < System.currentTimeMillis() + Duration.ofSeconds(3).toMillis()) continue

                    form.value.cancel("You have been inactive for 3 minutes, we're now cancelling this session.")
                    (form.value.model as Expireable).onExpire(form.value)

                }
            }, 10, 10, TimeUnit.SECONDS
        )
    }

    fun <T: FormModel> createForm(model: T, channel: MessageChannelAdapter, userId: Long): Form? {
        if (!hasForm(userId)) {
            Form(model, userId, channel).also {
                forms[userId] = it
                it.call()
            }
        }

        return forms[userId]
    }

    fun removeForm(id: Long) {
        forms.remove(id)
    }

    fun getForm(id: Long): Form? {
        if (!hasForm(id)) return null

        return forms[id]
    }

    fun hasForm(id: Long): Boolean {
        return forms.containsKey(id)
    }

    private val acknowledgedForms: Cache<Long, Form> = CacheBuilder.newBuilder()
        .expireAfterAccess(3, TimeUnit.MINUTES)
        .build()

    fun setAcknowledge(id: Long) {
        if (hasForm(id) && !hasAcknowledge(id)) {
            getForm(id)?.let {
                removeForm(id)
                acknowledgedForms.put(id, it)
            }
        }
    }

    fun removeAcknowledge(id: Long) {
        acknowledgedForms.invalidate(id)
    }

    fun getAcknowledge(id: Long): Form? {
        return acknowledgedForms.getIfPresent(id)
    }

    fun hasAcknowledge(id: Long): Boolean {
        return acknowledgedForms.asMap().containsKey(id)
    }
}