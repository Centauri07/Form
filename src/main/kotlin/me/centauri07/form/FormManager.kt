package me.centauri07.form

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalCause
import me.centauri07.form.adapter.channel.MessageChannelAdapter
import java.util.concurrent.TimeUnit

object FormManager {
    private val forms: Cache<Long, Form> = CacheBuilder.newBuilder()
        .expireAfterAccess(3, TimeUnit.MINUTES)
        .removalListener<Long, Form> {
            if (it.cause == RemovalCause.EXPIRED) {
                it.value?.cancel("You have been inactive for 3 minutes, we're now cancelling this session.")
                it.value?.let { form -> form.model.onExpire(form) }
            }
        }
        .build()

    fun <T: FormModel> createForm(model: T, channel: MessageChannelAdapter, userId: Long): Form? {
        if (!hasForm(userId)) {
            Form(model, userId, channel).also {
                forms.put(userId, it)
                it.call()
            }
        }

        return forms.getIfPresent(userId)
    }

    fun removeForm(id: Long) {
        forms.invalidate(id)
    }

    fun getForm(id: Long): Form? {
        if (!hasForm(id)) return null

        return forms.getIfPresent(id)
    }

    fun hasForm(id: Long): Boolean {
        return forms.asMap().containsKey(id)
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