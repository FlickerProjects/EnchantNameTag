package io.github.itsflicker.enchantnametag.module.listener

import io.github.itsflicker.enchantnametag.module.display.Scoreboard
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.expansion.releaseDataContainer

/**
 * @author wlys
 * @since 2022/2/27 11:54
 */
object ListenerPlayerQuit {

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        val player = e.player

        Scoreboard.teams.remove(player.name)
        player.releaseDataContainer()
    }
}