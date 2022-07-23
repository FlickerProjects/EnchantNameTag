package io.github.itsflicker.enchantnametag.module.hook

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * @author Arasple
 * @date 2021/1/26 22:02
 */
abstract class HookAbstract {

    open val name by lazy { listOf(getPluginName()) }

    val plugin: Plugin? by lazy {
        name.firstOrNull { Bukkit.getPluginManager().getPlugin(it) != null }?.let { Bukkit.getPluginManager().getPlugin(it) }
    }

    val isHooked by lazy {
        plugin != null && plugin!!.isEnabled
    }

    open fun getPluginName(): String {
        return javaClass.simpleName.substring(4)
    }

}