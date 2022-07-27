package io.github.itsflicker.enchantnametag.module.hook

import io.github.itsflicker.enchantnametag.module.hook.impl.HookItemsAdder
import io.github.itsflicker.enchantnametag.module.hook.impl.HookTAB
import taboolib.common.platform.function.console
import taboolib.module.lang.sendInfo

/**
 * @author Arasple
 * @date 2021/1/26 22:04
 */
object HookPlugin {

    fun printInfo() {
        registry.filter { it.isHooked }.forEach {
            console().sendInfo("plugin-dependency-hooked", it.name)
        }
    }

    private val registry: Array<HookAbstract> = arrayOf(
        HookTAB(),
        HookItemsAdder()
    )

    fun getTAB() = registry[0] as HookTAB

    fun getItemsAdder() = registry[1] as HookItemsAdder

}