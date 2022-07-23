package io.github.itsflicker.enchantnametag.module.database

import io.github.itsflicker.enchantnametag.EnchantNameTag
import org.bukkit.entity.Player
import taboolib.module.configuration.Configuration
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Type
import taboolib.module.database.*
import java.util.concurrent.ConcurrentHashMap

class DatabaseSQL : Database() {

    val host = EnchantNameTag.conf.getHost("Database.SQL")

    val table = Table(EnchantNameTag.conf.getString("Database.SQL.table")!!, host) {
        add {
            name("user")
            type(ColumnTypeSQL.VARCHAR, 36) {
                options(ColumnOptionSQL.PRIMARY_KEY)
            }
        }
        add {
            name("data")
            type(ColumnTypeSQL.MEDIUMTEXT)
        }
    }

    val dataSource = host.createDataSource()
    val cache = ConcurrentHashMap<String, Configuration>()

    init {
        table.workspace(dataSource) { createTable(true) }.run()
    }

    override fun pull(player: Player): ConfigurationSection {
        return cache.computeIfAbsent(player.name) {
            table.workspace(dataSource) {
                select { where { "user" eq player.name } }
            }.firstOrNull {
                Configuration.loadFromString(getString("data"))
            } ?: Configuration.empty(Type.YAML)
        }
    }

    override fun push(player: Player) {
        val file = cache[player.name] ?: return
        if (table.workspace(dataSource) { select { where { "user" eq player.name } } }.find()) {
            table.workspace(dataSource) {
                update {
                    set("data", file.saveToString())
                    where {
                        "user" eq player.name
                    }
                }
            }.run()
        } else {
            table.workspace(dataSource) {
                insert("user", "data") {
                    value(player.name, file.saveToString())
                }
            }.run()
        }
    }

    override fun release(player: Player) {
        cache.remove(player.name)
    }
}