package io.github.itsflicker.enchantnametag.module.listener

import io.github.itsflicker.enchantnametag.api.NMS
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.PacketSendEvent

/**
 * @author wlys
 * @since 2022/2/24 22:46
 */
object NMSListener {

    @SubscribeEvent
    fun e(e: PacketSendEvent) {
//        if (e.packet.name == "PacketPlayOutPlayerInfo") {
//            NMS.INSTANCE.processPlayerInfo(e.packet)
//        }
        if (e.packet.name == "PacketPlayOutScoreboardTeam") {
            NMS.INSTANCE.processScoreboardTeam(e.player, e.packet)
        }
    }
}