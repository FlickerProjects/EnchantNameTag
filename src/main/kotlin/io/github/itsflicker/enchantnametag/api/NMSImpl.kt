package io.github.itsflicker.enchantnametag.api

import io.github.itsflicker.enchantnametag.EnchantNameTag
import io.github.itsflicker.enchantnametag.module.display.Scoreboard
import io.github.itsflicker.enchantnametag.util.getDataContainer
import net.minecraft.server.v1_16_R3.*
import org.bukkit.entity.Player
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.Packet
import java.util.*

/**
 * @author wlys
 * @since 2022/2/24 22:36
 */
class NMSImpl : NMS() {

    override fun processPlayerInfo(packet: Packet) {
        val action = packet.read<PacketPlayOutPlayerInfo.EnumPlayerInfoAction>("action")!!
        if (action != PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER && action != PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME) {
            return
        }
        val list = packet.read<List<PacketPlayOutPlayerInfo.PlayerInfoData>>("entries")!!
        packet.write("entries", list.filter { it.a()?.name != null })
    }

    override fun processScoreboardTeam(player: Player, packet: Packet) {
        val method = packet.read<Int>("method")!!
        if (method != 0 && method != 2) {
            return
        }
        val name = packet.read<String>("name")!!
        if (!Scoreboard.teams.containsKey(name) && !(player.name in EnchantNameTag.loadedRP || player.getDataContainer().getBoolean("force_show", false))) {
            return
        }
        val optional = packet.read<Optional<*>>("parameters")!!
        if (!optional.isPresent) {
            return
        }
        val team = Scoreboard.teams[name]!!
        val parameters = optional.get()
        parameters.setProperty("playerPrefix", classChatSerializer.invokeMethod("b", team.prefix, isStatic = true))
        parameters.setProperty("playerSuffix", classChatSerializer.invokeMethod("b", team.suffix, isStatic = true))
        parameters.setProperty("color", EnumChatFormat.values()[team.color.ordinal])
    }

    override fun getMetaEntityBoolean(index: Int, value: Boolean): Any {
        return DataWatcher.Item(DataWatcherObject(index, DataWatcherRegistry.i), value)
    }

    override fun getMetaEntityChatBaseComponent(index: Int, json: String): Any {
        return DataWatcher.Item<Optional<IChatBaseComponent>>(
                DataWatcherObject(index, DataWatcherRegistry.f),
                Optional.ofNullable(classChatSerializer.invokeMethod("b", json, isStatic = true))
            )
    }
}