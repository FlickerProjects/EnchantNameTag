package io.github.itsflicker.enchantnametag

import io.github.itsflicker.enchantnametag.module.database.Database
import io.github.itsflicker.enchantnametag.module.database.DatabaseSQL
import io.github.itsflicker.enchantnametag.module.database.DatabaseSQLite
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import taboolib.platform.util.onlinePlayers

/**
 * @author wlys
 * @since 2021/9/11 13:29
 */
object Database {

    lateinit var database: Database

    fun init() {
        database = when (val type = EnchantNameTag.conf.getString("Database.Method")!!.uppercase()) {
            "SQLITE" -> DatabaseSQLite()
            "SQL" -> DatabaseSQL()
            else -> error("Unsupported database type: $type")
        }
    }

    @Schedule(delay = 100, period = 20 * 60 * 5, async = true)
    @Awake(LifeCycle.DISABLE)
    fun save() {
        onlinePlayers.forEach { database.push(it) }
    }
}