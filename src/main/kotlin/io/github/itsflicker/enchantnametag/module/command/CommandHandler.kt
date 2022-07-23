package io.github.itsflicker.enchantnametag.module.command

import io.github.itsflicker.enchantnametag.EnchantNameTag
import io.github.itsflicker.enchantnametag.module.command.impl.CommandEquip
import io.github.itsflicker.enchantnametag.module.command.impl.CommandListAll
import io.github.itsflicker.enchantnametag.module.conf.Loader
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand

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

    @CommandBody(permission = "enchantnametag.command.reload")
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Loader.loadResources(sender)
            EnchantNameTag.updateAllNameTags()
        }
    }
}