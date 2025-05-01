package com.example.urtisi

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object NewsCache {
    private const val FILE_NAME = "news_cache.json"
    private const val MAX_CACHE_AGE_MS = 7 * 24 * 60 * 60 * 1000 // 1 неделя

    fun save(context: Context, newsList: List<NewsItem>) {
        try {
            val cacheData = NewsCacheData(System.currentTimeMillis(), newsList)
            val json = Gson().toJson(cacheData)
            File(context.filesDir, FILE_NAME).writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(context: Context): List<NewsItem> {
        return try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) return emptyList()

            val json = file.readText()
            val type = object : TypeToken<NewsCacheData>() {}.type
            val cacheData = Gson().fromJson<NewsCacheData>(json, type)

            // Проверяем, не устарел ли кэш
            if (System.currentTimeMillis() - cacheData.timestamp > MAX_CACHE_AGE_MS) {
                file.delete()
                emptyList()
            } else {
                cacheData.newsList
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private data class NewsCacheData(
        val timestamp: Long,
        val newsList: List<NewsItem>
    )
}