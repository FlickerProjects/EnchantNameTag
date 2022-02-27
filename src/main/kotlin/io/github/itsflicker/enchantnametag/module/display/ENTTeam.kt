package io.github.itsflicker.enchantnametag.module.display

import io.github.itsflicker.enchantnametag.module.conf.font.Font
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import taboolib.expansion.getDataContainer

/**
 * @author wlys
 * @since 2022/2/26 18:01
 */
class ENTTeam(val player: Player) {

    val team by lazy { Scoreboard.scoreboard.getTeam(player.name) ?: Scoreboard.scoreboard.registerNewTeam(player.name) }

    var prefix = "{\"text\":\"\"}"
    var suffix = "{\"text\":\"\"}"
    var color = ChatColor.WHITE

    var isHidden = false

    init {
        team.addEntry(player.name)
        show()
        updateNameTag()
    }

    fun updateNameTag() {
        val id = player.getDataContainer()["equipped_tag"].let {
            if (it == null || it.equals("NONE", ignoreCase = true)) {
                hide()
                return
            } else {
                it
            }
        }
        val font = Font.fonts.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: kotlin.run {
            hide()
            return
        }
        if (!player.hasPermission(font.permission)) {
            player.getDataContainer()["equipped_tag"] = "NONE"
            hide()
            return
        }
        show()

        font.generatePrefixAndSuffix(player).let {
            prefix = it.first
            suffix = it.second
        }
        color = font.color

        team.prefix = ""

    }

    fun hide() {
        if (!isHidden) {
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
            isHidden = true
        }
    }

    fun show() {
        if (isHidden) {
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
            isHidden = false
        }
    }
}