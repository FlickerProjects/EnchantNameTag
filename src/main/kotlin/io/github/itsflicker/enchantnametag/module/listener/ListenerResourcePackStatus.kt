package io.github.itsflicker.enchantnametag.module.listener

import io.github.itsflicker.enchantnametag.EnchantNameTag
import io.github.itsflicker.enchantnametag.util.updateAllNameTags
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author wlys
 * @since 2022/2/27 16:07
 */
object ListenerResourcePackStatus {

    @SubscribeEvent
    fun e(e: PlayerResourcePackStatusEvent) {
        if (e.status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            EnchantNameTag.loadedRP += e.player.name
            updateAllNameTags()
        }
    }
}