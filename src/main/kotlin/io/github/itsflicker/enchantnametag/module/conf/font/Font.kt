package io.github.itsflicker.enchantnametag.module.conf.font

import io.github.itsflicker.enchantnametag.module.hook.HookPlugin
import io.github.itsflicker.enchantnametag.util.jsonArray
import io.github.itsflicker.enchantnametag.util.jsonFormatWithFont
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.warning
import taboolib.library.xseries.getItemStack
import taboolib.module.configuration.Configuration
import taboolib.platform.compat.replacePlaceholder

/**
 * @author wlys
 * @since 2022/2/26 12:16
 */
class Font(
    val id: String,
    val caches: MutableList<FontCache>,
    val conf: Configuration
) {

    val displayItem by lazy {
        conf.getItemStack("display") ?: ItemStack(Material.STONE)
    }

    val permission by lazy {
        conf.getString("permission", "enchantnametag.tag.$id")!!
    }

    val prefixTemplate by lazy {
        conf.getString("prefix", "")!!
    }

    val suffixTemplate by lazy {
        conf.getString("suffix", "")!!
    }

    val color by lazy {
        kotlin.runCatching { ChatColor.valueOf(conf.getString("color", "WHITE")!!.uppercase()) }.getOrElse {
            warning("Unknown color in $id.yml")
            warning("Available colors: ${ChatColor.values().joinToString(", ") { it.name }}")
            ChatColor.WHITE
        }
    }

    val period by lazy {
        conf.getLong("period", 10)
    }

    private var index = 0

    val cache get() =
        if (caches.size > 1) {
            if (index < caches.size) {
                index += 1
                caches[index - 1]
            } else {
                index = 1
                caches[index - 1]
            }
        } else {
            caches.first()
        }

    init {
        caches.sortBy { it.index }
        color
    }

    fun generatePrefixAndSuffix(player: Player): Pair<String, String> {
        val cache = cache
        val p = HookPlugin.getTAB().getSuffix(player) ?: prefixTemplate.replacePlaceholder(player)
        val s = HookPlugin.getTAB().getPrefix(player) ?: suffixTemplate.replacePlaceholder(player)
        val prefix = jsonArray(
            jsonFormatWithFont(
                ChatColor.WHITE.toString() + cache.makeCustomNameTag(p, player.name, s),
                "enchantnametag:default"
            ),
            jsonFormatWithFont(ChatColor.WHITE.toString() + p, "minecraft:default")
        )
        val suffix = jsonArray(
            jsonFormatWithFont(ChatColor.WHITE.toString() + s, "minecraft:default"),
            jsonFormatWithFont(
                ChatColor.WHITE.toString() + cache.getSuffixLength(p + player.name + s),
                "enchantnametag:default"
            )
        )
        return prefix to suffix
    }

    companion object {

        val fonts = mutableListOf<Font>()

        val EMPTY = Font("null", mutableListOf(), Configuration.empty())
    }
}