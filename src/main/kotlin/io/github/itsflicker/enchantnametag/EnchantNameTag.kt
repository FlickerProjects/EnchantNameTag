package io.github.itsflicker.enchantnametag

import io.github.itsflicker.enchantnametag.module.conf.Loader
import io.github.itsflicker.enchantnametag.module.display.getOrCreateTeam
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.common.platform.function.onlinePlayers
import taboolib.expansion.setupPlayerDatabase
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin

object EnchantNameTag : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    @Config(autoReload = true)
    lateinit var conf: Configuration
        private set

    override fun onEnable() {
        if (conf.getBoolean("Database.enable")) {
            setupPlayerDatabase(conf.getConfigurationSection("Database")!!)
        } else {
            setupPlayerDatabase(newFile(conf.getString("file", "{plugin_folder}/data.db")!!.replace("{plugin_folder}", getDataFolder().path)))
        }
        Loader.loadResources(console())
        onlinePlayers().forEach { it.cast<Player>().getOrCreateTeam() }
    }
}