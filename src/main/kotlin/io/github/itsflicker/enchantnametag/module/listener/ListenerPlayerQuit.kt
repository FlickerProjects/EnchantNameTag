package io.github.itsflicker.enchantnametag.module.listener

import io.github.itsflicker.enchantnametag.Database
import io.github.itsflicker.enchantnametag.module.display.Scoreboard
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

/**
 * @author wlys
 * @since 2022/2/27 11:54
 */
object ListenerPlayerQuit {

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        val player = e.player

        Scoreboard.teams.remove(player.name)?.task?.cancel()
        submit(async = true) {
            Database.database.push(player)
            Database.database.release(player)
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun e(e: PlayerKickEvent) {
        val player = e.player

        Scoreboard.teams.remove(player.name)?.task?.cancel()
        submit(async = true) {
            Database.database.push(player)
            Database.database.release(player)
        }
    }
}