package io.github.itsflicker.enchantnametag

import io.github.itsflicker.enchantnametag.module.conf.Loader
import io.github.itsflicker.enchantnametag.module.display.getOrCreateTeam
import io.github.itsflicker.enchantnametag.module.hook.HookPlugin
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import taboolib.platform.util.onlinePlayers

object EnchantNameTag : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    @Config(autoReload = true)
    lateinit var conf: Configuration
        private set

    override fun onEnable() {
        Database.init()
        Loader.loadResources(console())
        HookPlugin.printInfo()
        updateAllNameTags()
    }

    fun updateAllNameTags() {
        onlinePlayers.forEach { it.getOrCreateTeam() }
    }
}