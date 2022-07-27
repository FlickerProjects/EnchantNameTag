package io.github.itsflicker.enchantnametag.module.command

import io.github.itsflicker.enchantnametag.module.command.impl.CommandEquip
import io.github.itsflicker.enchantnametag.module.command.impl.CommandListAll
import io.github.itsflicker.enchantnametag.module.conf.Loader
import io.github.itsflicker.enchantnametag.module.conf.font.Font
import io.github.itsflicker.enchantnametag.module.display.getOrCreateTeam
import io.github.itsflicker.enchantnametag.util.getDataContainer
import io.github.itsflicker.enchantnametag.util.updateAllNameTags
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import taboolib.platform.util.asLangText
import taboolib.platform.util.sendLang

/**
 * @author wlys
 * @since 2022/2/26 13:25
 */
@CommandHeader("enchantnametag", ["ent"], permission = "enchantnametag.access")
object CommandHandler {

    @CommandBody(permission = "enchantnametag.command.listall")
    val listall = CommandListAll.command

    @CommandBody(permission = "enchantnametag.command.equip")
    val equip = CommandEquip.command

    @CommandBody(permission = "enchantnametag.command.forceequip")
    val forceequip = subCommand {
        dynamic("player") {
            suggestPlayers()
            dynamic("nametag") {
                suggest {
                    Font.fonts.map { it.id }
                }
                execute<CommandSender> { _, content, argument ->
                    val player = Bukkit.getPlayer(content.argument(-1))!!
                    player.getDataContainer()["equipped_tag"] = argument
                    player.getOrCreateTeam()
                }
            }
        }
    }

    @CommandBody(permission = "enchantnametag.command.equip")
    val forceshow = subCommand {
        execute<Player> { sender, _, _ ->
            val status = !sender.getDataContainer().getBoolean("force_show", false)
            sender.getDataContainer()["force_show"] = status
            sender.sendLang("info-force-show", status.print(sender))
            updateAllNameTags()
        }
    }

    @CommandBody(permission = "enchantnametag.command.reload")
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Loader.loadResources(sender)
            updateAllNameTags()
        }
    }

    private fun Boolean.print(cs: CommandSender) = if (this) cs.asLangText("info-on") else cs.asLangText("info-off")
}