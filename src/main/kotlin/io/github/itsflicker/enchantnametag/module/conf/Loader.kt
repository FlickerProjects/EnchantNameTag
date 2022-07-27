package io.github.itsflicker.enchantnametag.module.conf

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.itsflicker.enchantnametag.EnchantNameTag
import io.github.itsflicker.enchantnametag.module.conf.font.Font
import io.github.itsflicker.enchantnametag.module.conf.font.FontCache
import io.github.itsflicker.enchantnametag.module.conf.font.FontInfo
import io.github.itsflicker.enchantnametag.module.hook.HookPlugin
import io.github.itsflicker.enchantnametag.util.NegativeFont
import io.github.itsflicker.enchantnametag.util.native2ascii
import taboolib.common.io.deepDelete
import taboolib.common.io.newFile
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import taboolib.module.lang.sendInfo
import java.io.File
import kotlin.system.measureTimeMillis

/**
 * @author wlys
 * @since 2022/2/25 22:15
 */
object Loader {

    private val folder by lazy {
        val folder = File(getDataFolder(), "resources")

        if (!folder.exists()) {
            arrayOf(
                "crown",
                "yellow"
            ).forEach {
                releaseResourceFile("resources/$it.png", replace = true)
                releaseResourceFile("resources/$it.yml", replace = true)
            }
        }

        folder
    }

    fun loadResources(sender: ProxyCommandSender) {
        measureTimeMillis { loadResources() }.let {
            sender.sendInfo("resource-pack-generated", Font.fonts.size, it)
        }
    }

    fun loadResources() {
        Font.fonts.clear()

        val generated = File(getDataFolder(), "generated")
        if (generated.exists()) {
            generated.deepDelete()
        }
        newFile(generated, folder = true)
        val fontFolder = newFile(generated.path + File.separatorChar + "enchantnametag" + File.separatorChar + "font", folder = true)
        val texturesFolder = newFile(generated.path + File.separatorChar + "enchantnametag" + File.separatorChar + "textures", folder = true)

        var c = EnchantNameTag.conf.getString("initial-substitution", "\uf100")!![0]
        val font = mutableMapOf<String, Pair<MutableList<FontCache>, Configuration>>()

        val json = JsonObject().apply {
            val jsonArray = JsonArray()
            filterFontFiles(folder).sorted().forEach {
                val info = FontInfo(c.also { c += 1 }, c.also { c += 1 }, c.also { c += 1 })
                val (id, cache) = loadResource(it, info)
                val conf = Configuration.loadFromFile(newFile(folder, "$id.yml"))
                font.computeIfAbsent(id) { ArrayList<FontCache>() to conf }.first.add(cache)

                jsonArray.add(JsonObject().apply {
                    addProperty("type", "bitmap")
                    addProperty("file", "enchantnametag:${it.name.lowercase()}")
                    addProperty("ascent", 12)
                    addProperty("height", conf.getInt("height", 16))
                    add("chars", JsonArray().apply {
                        add(native2ascii(info.left) + native2ascii(info.middle) + native2ascii(info.right))
                    })
                })

                it.copyTo(File(texturesFolder, it.name.lowercase()))
            }
            NegativeFont.values().map { getNegativeFontChar(it.height, it.char) }.forEach { jsonArray.add(it) }
            add("providers", jsonArray)
        }
        releaseResourceFile("generated/enchantnametag/textures/space_split.png")
        val default = newFile(fontFolder, "default.json")
        default.writeText(json.toString().replace("\\\\", "\\"))
        HookPlugin.getItemsAdder().copyToIA(generated)

        font.entries.forEach { (id, caches) ->
            Font.fonts.add(Font(id, caches.first, caches.second))
        }
    }

    fun loadResource(file: File, info: FontInfo): Pair<String, FontCache> {
        val name = file.nameWithoutExtension
        val (id, index) = name.split('-').let {
            it[0] to (it.getOrNull(1)?.toInt() ?: 0)
        }

        return id to FontCache(info, index)
    }

    private fun getNegativeFontChar(height: Int, char: Char): JsonObject {
        return JsonObject().apply {
            addProperty("type", "bitmap")
            addProperty("file", "enchantnametag:space_split.png")
            addProperty("ascent", -5000)
            addProperty("height", height)
            add("chars", JsonArray().apply {
                add(native2ascii(char))
            })
        }
    }

    private fun filterFontFiles(file: File): List<File> {
        return mutableListOf<File>().apply {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    addAll(filterFontFiles(it))
                }
            } else if (file.extension.equals("png", true)) {
                add(file)
            }
        }
    }
}