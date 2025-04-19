package com.example.urtisi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import android.util.Log

object NewsParser {
    suspend fun parseNews(): List<NewsItem> = withContext(Dispatchers.IO) {
        val newsList = mutableListOf<NewsItem>()
        try {
            Log.d("NewsParser", "Starting to parse news")

            val doc = Jsoup.connect("https://www.uisi.ru/uisi/general/news.php")
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get()

            val elements = doc.select("p.news-item")
            Log.d("NewsParser", "Found ${elements.size} news items")

            for (element in elements) {
                try {
                    val titleElement = element.select("a").first()
                    val title = titleElement?.text()?.trim() ?: ""
                    val link = "https://www.uisi.ru" + (titleElement?.attr("href")?.trim() ?: "")
                    val date = element.select("span.news-date-time").text().trim()

                    if (title.isNotEmpty() && date.isNotEmpty()) {
                        newsList.add(NewsItem(title, date, link))
                        Log.d("NewsParser", "Added: $date - $title")
                    }
                } catch (e: Exception) {
                    Log.e("NewsParser", "Error parsing single news item", e)
                }
            }
        } catch (e: Exception) {
            Log.e("NewsParser", "Error parsing news", e)
        }

        Log.d("NewsParser", "Finished parsing. Total items: ${newsList.size}")
        return@withContext newsList
    }
}