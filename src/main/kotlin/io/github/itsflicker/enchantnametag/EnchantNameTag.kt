package io.github.itsflicker.enchantnametag

import io.github.itsflicker.enchantnametag.module.conf.Loader
import io.github.itsflicker.enchantnametag.module.hook.HookPlugin
import io.github.itsflicker.enchantnametag.util.updateAllNameTags
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object EnchantNameTag : Plugin() {

    val loadedRP = mutableListOf<String>()

    @Config(autoReload = true)
    lateinit var conf: Configuration
        private set

    override fun onEnable() {
        Database.init()
        HookPlugin.printInfo()
        Loader.loadResources(console())
        updateAllNameTags()
    }
}