package io.github.itsflicker.enchantnametag.module.display

import io.github.itsflicker.enchantnametag.getDataContainer
import io.github.itsflicker.enchantnametag.module.conf.font.Font
import io.github.itsflicker.enchantnametag.util.teamName
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import kotlin.properties.Delegates

/**
 * @author wlys
 * @since 2022/2/26 18:01
 */
class ENTTeam(val player: Player) {

    private val team by lazy { Scoreboard.scoreboard.getTeam(player.teamName) ?: Scoreboard.scoreboard.registerNewTeam(player.teamName) }

    var task: PlatformExecutor.PlatformTask? = null

    var font0 by Delegates.observable(Font.EMPTY) { _, oldValue, newValue ->
        if (newValue == oldValue) {
            updateDisplay()
            return@observable
        }
        task?.cancel()
        task = null
        if (newValue.caches.size > 1) {
            task = submit(async = true, period = newValue.period) {
                updateNameTag(newValue)
            }
        } else {
            updateNameTag(newValue)
        }
    }

    var prefix = "{\"text\":\"\"}"
    var suffix = "{\"text\":\"\"}"
    var color = ChatColor.WHITE

    var isHidden = false

    init {
        team.addEntry(player.name)
        check()
        show()
    }

    fun check() {
        val id = player.getDataContainer().getString("equipped_tag").let {
            if (it == null || it.equals("NONE", ignoreCase = true)) {
                return
            } else {
                it
            }
        }
        val font = Font.fonts.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: kotlin.run {
            return
        }
        if (!player.hasPermission(font.permission)) {
            player.getDataContainer()["equipped_tag"] = "NONE"
            return
        }
        font0 = font
    }

    fun updateNameTag(font: Font) {
        font.generatePrefixAndSuffix(player).let {
            prefix = it.first
            suffix = it.second
        }
        color = font.color

        updateDisplay()
    }

    fun updateDisplay() {
        team.prefix = ""
    }

    fun hide() {
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
        isHidden = true
    }

    fun show() {
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
        isHidden = false
    }
}