package io.github.itsflicker.enchantnametag.module.command.impl

import io.github.itsflicker.enchantnametag.getDataContainer
import io.github.itsflicker.enchantnametag.module.conf.font.Font
import io.github.itsflicker.enchantnametag.module.display.getOrCreateTeam
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XMaterial
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.util.buildItem
import taboolib.platform.util.inventoryCenterSlots

/**
 * @author wlys
 * @since 2022/2/26 19:12
 */
object CommandEquip {

    val command = subCommand {
        execute<Player> { sender, _, _ ->
            openMenu(sender)
        }
    }

    private fun openMenu(sender: Player) {
        sender.openMenu<Linked<Font>>("All Name Tags") {
            rows(6)
            slots(inventoryCenterSlots)
            elements {
                Font.fonts
            }
            setPreviousPage(47) { _, hasPreviousPage ->
                if (hasPreviousPage) {
                    buildItem(XMaterial.SPECTRAL_ARROW) { name = "§8上一页"}
                } else {
                    buildItem(XMaterial.ARROW) { name = "§8上一页"}
                }
            }
            setNextPage(51) { _, hasNextPage ->
                if (hasNextPage) {
                    buildItem(XMaterial.SPECTRAL_ARROW) { name = "§8下一页"}
                } else {
                    buildItem(XMaterial.ARROW) { name = "§8下一页"}
                }
            }
            onGenerate { player, element, _, _ ->
                if (player.hasPermission(element.permission)) {
                    buildItem(element.displayItem) {
                        lore += ""
                        lore += "§2> Click to equip"
                        if (player.getDataContainer()["equipped_tag"] == element.id) {
                            shiny()
                        }
                    }
                } else {
                    buildItem(XMaterial.BARRIER) {
                        name = element.displayItem.itemMeta.displayName
                        lore += ""
                        lore += "§cYou haven't got this tag!"
                    }
                }
            }
            set(49, buildItem(Material.NAME_TAG) {
                name = "§3Current tag: §f${sender.getDataContainer()["equipped_tag"] ?: "NONE"}"
                lore += ""
                lore += "§2> Click to remove current tag"
            }) {
                clicker.getDataContainer()["equipped_tag"] = "NONE"
                clicker.getOrCreateTeam()
                openMenu(clicker)
            }
            onClick { clickEvent, element ->
                val player = clickEvent.clicker
                player.getDataContainer()["equipped_tag"] = element.id
                player.getOrCreateTeam()
                openMenu(player)
            }
        }
    }
}