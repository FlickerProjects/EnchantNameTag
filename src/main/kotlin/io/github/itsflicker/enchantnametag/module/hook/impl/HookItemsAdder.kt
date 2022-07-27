package io.github.itsflicker.enchantnametag.module.hook.impl

import io.github.itsflicker.enchantnametag.module.hook.HookAbstract
import me.neznamy.tab.api.TabAPI
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.platform.function.console
import taboolib.module.lang.sendError
import taboolib.module.lang.sendInfo
import java.io.File

/**
 * @author wlys
 * @since 2022/3/12 20:29
 */
class HookItemsAdder : HookAbstract() {

    private val separator = File.separator

    fun copyToIA(folder: File) {
        if (isHooked) {
            val target = File(plugin!!.dataFolder.path+separator+"data"+separator+"resource_pack"+separator+"assets")
            if (File(target, "enchantnametag").exists()) {
                File(target, "enchantnametag").deleteRecursively()
            }
            folder.copyRecursively(target) { file, e ->
                console().sendError("plugin-dependency-itemsadder-error", file.name)
                e.printStackTrace()
                OnErrorAction.SKIP
            }
            console().sendInfo("plugin-dependency-itemsadder-success")
        }
    }

}