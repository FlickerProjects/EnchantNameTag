package io.github.itsflicker.enchantnametag.module.listener

import io.github.itsflicker.enchantnametag.module.display.getOrCreateTeam
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.onlinePlayers

/**
 * @author wlys
 * @since 2022/2/27 16:07
 */
object ListenerResourcePackStatus {

    @SubscribeEvent
    fun e(e: PlayerResourcePackStatusEvent) {
        if (e.status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            onlinePlayers().forEach { it.cast<Player>().getOrCreateTeam() }
        }
    }
}