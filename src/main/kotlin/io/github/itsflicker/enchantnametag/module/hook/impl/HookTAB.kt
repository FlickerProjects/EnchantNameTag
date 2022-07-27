package io.github.itsflicker.enchantnametag.module.hook.impl

import io.github.itsflicker.enchantnametag.module.hook.HookAbstract
import me.neznamy.tab.api.TabAPI
import org.bukkit.entity.Player

/**
 * @author wlys
 * @since 2022/3/12 20:29
 */
class HookTAB : HookAbstract() {

    fun getTeam(player: Player): String? {
        return if (!isHooked) {
            null
        } else {
            TabAPI.getInstance().getPlayer(player.name)?.teamName
        }
    }

    fun getPrefix(player: Player): String? {
        return if (!isHooked) {
            null
        } else {
            TabAPI.getInstance().teamManager?.getCustomPrefix(TabAPI.getInstance().getPlayer(player.name) ?: return null)
        }
    }

    fun getSuffix(player: Player): String? {
        return if (!isHooked) {
            null
        } else {
            TabAPI.getInstance().teamManager?.getCustomSuffix(TabAPI.getInstance().getPlayer(player.name) ?: return null)
        }
    }
}