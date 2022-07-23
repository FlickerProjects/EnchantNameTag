package io.github.itsflicker.enchantnametag.module.listener

import io.github.itsflicker.enchantnametag.EnchantNameTag
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author wlys
 * @since 2022/2/26 18:36
 */
object ListenerPlayerJoin {

    @SubscribeEvent(EventPriority.MONITOR, ignoreCancelled = true)
    fun e(e: PlayerJoinEvent) {
        EnchantNameTag.updateAllNameTags()
    }
}