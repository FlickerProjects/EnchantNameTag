package io.github.itsflicker.enchantnametag.module.conf.font

import io.github.itsflicker.enchantnametag.util.MinecraftFontWidth
import io.github.itsflicker.enchantnametag.util.NegativeFont
import net.md_5.bungee.api.ChatColor
import taboolib.module.chat.uncolored

/**
 * @author wlys
 * @since 2022/2/26 12:38
 */
class FontCache(
    val info: FontInfo,
    val index: Int
) {

    fun makeCustomNameTag(prefix: String, name: String, suffix: String): String {
        val str = prefix + name + suffix
        val i = MinecraftFontWidth.getTotalWidth(ChatColor.stripColor(str))
        val odd = i % 2 == 0
        val left = info.left
        val middle = info.middle
        val right = info.right
        val neg1 = NegativeFont.NEG_1.char
        val j = i + 16 + 1
        val stringBuilder = StringBuilder()
        stringBuilder.append(NegativeFont.getShortestSequenceForLength(if (odd) j else j + 1))
        stringBuilder.append(left).append(neg1)
        val k = (i + 1) / 16
        val m = (i + 1) % 16 + if (odd) 0 else 1
        for (b2 in 0 until if (k == 0) 1 else k) stringBuilder.append(middle).append(neg1)
        stringBuilder.append(NegativeFont.getShortestSequenceForLength(16 - m))
        stringBuilder.append(middle).append(neg1)
        stringBuilder.append(right).append(neg1)
        stringBuilder.append(NegativeFont.getShortestSequenceForLength(if (odd) j else j + 1))

        return stringBuilder.toString()
    }

    fun getSuffixLength(name: String): String {
        val i = MinecraftFontWidth.getTotalWidth(name.uncolored())
        return NegativeFont.getShortestSequenceForLength(i + i % 2 + 1)
    }
}