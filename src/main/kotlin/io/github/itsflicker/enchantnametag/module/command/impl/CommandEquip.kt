package io.github.itsflicker.enchantnametag.module.command.impl

import io.github.itsflicker.enchantnametag.util.getDataContainer
import io.github.itsflicker.enchantnametag.module.conf.font.Font
import io.github.itsflicker.enchantnametag.module.display.getOrCreateTeam
import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.library.xseries.XMaterial
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.util.asLangText
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
        sender.openMenu<Linked<Font>>(sender.asLangText("menu-equip-title")) {
            rows(6)
            slots(inventoryCenterSlots)
            elements {
                Font.fonts
            }
            setPreviousPage(47) { _, hasPreviousPage ->
                if (hasPreviousPage) {
                    buildItem(XMaterial.SPECTRAL_ARROW) { name = sender.asLangText("menu-previous-page") }
                } else {
                    buildItem(XMaterial.ARROW) { name = sender.asLangText("menu-previous-page") }
                }
            }
            setNextPage(51) { _, hasNextPage ->
                if (hasNextPage) {
                    buildItem(XMaterial.SPECTRAL_ARROW) { name = sender.asLangText("menu-next-page") }
                } else {
                    buildItem(XMaterial.ARROW) { name = sender.asLangText("menu-next-page") }
                }
            }
            onGenerate { player, element, _, _ ->
                if (player.hasPermission(element.permission)) {
                    buildItem(element.displayItem) {
                        lore += ""
                        lore += sender.asLangText("menu-equip-equip")
                        if (player.getDataContainer()["equipped_tag"] == element.id) {
                            shiny()
                        }
                    }
                } else {
                    buildItem(XMaterial.BARRIER) {
                        name = element.displayItem.itemMeta.displayName
                        lore += ""
                        lore += sender.asLangText("menu-equip-unavailable")
                    }
                }
            }
            set(49, buildItem(Material.NAME_TAG) {
                name = sender.asLangText("menu-equip-current", sender.getDataContainer()["equipped_tag"] ?: sender.asLangText("menu-equip-none"))
                lore += ""
                lore += sender.asLangText("menu-equip-remove")
            }) {
                clicker.getDataContainer()["equipped_tag"] = null
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