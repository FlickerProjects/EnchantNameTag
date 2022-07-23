package io.github.itsflicker.enchantnametag.module.command.impl

import io.github.itsflicker.enchantnametag.api.NMS
import io.github.itsflicker.enchantnametag.module.conf.font.Font
import io.github.itsflicker.enchantnametag.util.jsonFormatWithFont
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.nmsClass
import taboolib.module.nms.sendPacket
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked
import taboolib.platform.util.buildItem
import taboolib.platform.util.inventoryCenterSlots

/**
 * @author wlys
 * @since 2022/2/26 19:11
 */
object CommandListAll {

    private val classPacketPlayOutEntityMetadata by lazy {
        nmsClass("PacketPlayOutEntityMetadata")
    }

    val command = subCommand {
        execute<Player> { sender, _, _ ->
            sender.openMenu<Linked<Font>>("All Name Tags") {
                rows(6)
                slots(inventoryCenterSlots)
                elements { Font.fonts }
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
                onGenerate { _, element, _, _ ->
                    buildItem(element.displayItem) {
                        lore += ""
                        lore += "§2> Click to preview"
                    }
                }
                onClick { _, element ->
                    val armorStand = sender.world.spawn(sender.location, ArmorStand::class.java) {
                        it.isInvisible = true
                        it.isCollidable = false
                        it.isInvulnerable = true
                        it.isVisible = false
                        it.isCustomNameVisible = false
                        it.isSmall = true
                    }
                    var (prefix, suffix) = element.generatePrefixAndSuffix(sender)
                    prefix = prefix.replace("[\\[\\]]", "")
                    suffix = suffix.replace("[\\[\\]]", "")
                    val stringBuilder = StringBuilder("[")
                    if (prefix.isNotEmpty()) {
                        stringBuilder.append(prefix).append(",")
                    }
                    stringBuilder
                        .append(jsonFormatWithFont(element.color.toString() + sender.name, "minecraft:default"))
                        .append(",")
                    if (suffix.isNotEmpty()) {
                        stringBuilder.append(suffix).append("]")
                    }
                    val packet = classPacketPlayOutEntityMetadata.unsafeInstance().also {
                        it.setProperty("id", armorStand.entityId)
                        it.setProperty("packedItems", listOf(
                            NMS.INSTANCE.getMetaEntityChatBaseComponent(2, stringBuilder.toString()),
                            NMS.INSTANCE.getMetaEntityBoolean(3, true)
                        ))
                    }
                    sender.sendPacket(packet)
                    submit(delay = 5 * 20) {
                        armorStand.remove()
                    }
                    sender.closeInventory()
                }
            }
        }
    }
}