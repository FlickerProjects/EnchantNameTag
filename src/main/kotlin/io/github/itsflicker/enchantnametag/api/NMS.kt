package io.github.itsflicker.enchantnametag.api

import org.bukkit.entity.Player
import taboolib.module.nms.Packet
import taboolib.module.nms.nmsClass
import taboolib.module.nms.nmsProxy

/**
 * @author wlys
 * @since 2022/2/24 22:35
 */
abstract class NMS {

    val classChatSerializer by lazy {
        nmsClass("IChatBaseComponent\$ChatSerializer")
    }

    abstract fun processPlayerInfo(packet: Packet)

    abstract fun processScoreboardTeam(player: Player, packet: Packet)

    abstract fun getMetaEntityBoolean(index: Int, value: Boolean): Any

    abstract fun getMetaEntityChatBaseComponent(index: Int, json: String): Any

    companion object {

        val INSTANCE by lazy { nmsProxy<NMS>() }
    }
}