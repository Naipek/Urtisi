package com.example.urtisi

import android.content.Context
import com.google.gson.Gson
import java.io.File

object NewsDetailCache {
    private const val CACHE_DIR = "news_details"

    fun save(context: Context, item: NewsFullItem) {
        try {
            // Создаем директорию для кэша, если ее нет
            val cacheDir = File(context.filesDir, CACHE_DIR)
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val json = Gson().toJson(item)
            val filename = hashFilename(item.url)
            File(cacheDir, filename).writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(context: Context, url: String): NewsFullItem? {
        return try {
            val cacheDir = File(context.filesDir, CACHE_DIR)
            val file = File(cacheDir, hashFilename(url))
            if (!file.exists()) return null

            val json = file.readText()
            Gson().fromJson(json, NewsFullItem::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun hashFilename(url: String): String {
        return "detail_" + url.hashCode().toString().replace("-", "n") + ".json"
    }
}