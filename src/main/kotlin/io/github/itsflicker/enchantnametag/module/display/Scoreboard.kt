package io.github.itsflicker.enchantnametag.module.display

import io.github.itsflicker.enchantnametag.util.teamName
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

/**
 * @author wlys
 * @since 2022/2/26 14:23
 */
object Scoreboard {

    val scoreboard by lazy { Bukkit.getScoreboardManager().mainScoreboard }

    val teams = mutableMapOf<String, ENTTeam>()
}

fun Player.getOrCreateTeam(): ENTTeam {
    return Scoreboard.teams.computeIfAbsent(teamName) { ENTTeam(this) }.also {
        submit(delay = 20L) {
            it.check()
        }
    }
}