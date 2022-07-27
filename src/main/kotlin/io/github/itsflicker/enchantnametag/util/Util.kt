package io.github.itsflicker.enchantnametag.util

import io.github.itsflicker.enchantnametag.Database
import io.github.itsflicker.enchantnametag.module.display.getOrCreateTeam
import io.github.itsflicker.enchantnametag.module.hook.HookPlugin
import org.bukkit.entity.Player
import taboolib.library.configuration.ConfigurationSection
import taboolib.platform.util.onlinePlayers

/**
 * @author wlys
 * @since 2022/2/26 22:26
 */
fun jsonFormatWithFont(string: String, font: String) = "{\"text\":\"$string\",\"font\":\"$font\"}"

fun jsonArray(vararg items: String) = "[${items.joinToString(",")}]"

fun native2ascii(paramString: String): String {
    val stringBuilder = StringBuilder()
    for (c in paramString.toCharArray()) stringBuilder.append(native2ascii(c))
    return stringBuilder.toString()
}

fun native2ascii(paramChar: Char): String {
    if (paramChar > '') {
        val stringBuilder1 = StringBuilder()
        stringBuilder1.append("\\u")
        val stringBuilder2 = StringBuilder(Integer.toHexString(paramChar.code))
        stringBuilder2.reverse()
        val i = 4 - stringBuilder2.length
        var b: Byte = 0
        while (b < i) {
            stringBuilder2.append('0')
            b++
        }
        b = 0
        while (b < 4) {
            stringBuilder1.append(stringBuilder2[3 - b])
            b++
        }
        return stringBuilder1.toString()
    }
    return paramChar.toString()
}

val Player.teamName get() = HookPlugin.getTAB().getTeam(this) ?: name

fun Player.getDataContainer(): ConfigurationSection {
    return Database.database.pull(this)
}

fun updateAllNameTags() {
    onlinePlayers.forEach { it.getOrCreateTeam() }
}