package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var newsTitle: TextView
    private lateinit var newsDate: TextView
    private lateinit var newsContent: TextView
    private lateinit var newsImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        // Инициализация элементов
        progressBar = findViewById(R.id.progressBar)
        newsTitle = findViewById(R.id.newsTitle)
        newsDate = findViewById(R.id.newsDate)
        newsContent = findViewById(R.id.newsContent)
        newsImage = findViewById(R.id.newsImage)


        // Подключение нижней панели
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Загрузка данных
        loadNewsContent(intent.getStringExtra("url") ?: "")
    }

    private fun loadNewsContent(url: String) {
        progressBar.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url).get()

                // Парсинг данных
                val title = doc.select("div.news-detail h3").text()
                val date = doc.select("span.news-date-time").text()
                val content = doc.select("div.news-detail div[align=justify]").text()
                val imageUrl = doc.select("div.news-detail img").attr("src")

                runOnUiThread {
                    // Установка данных
                    newsTitle.text = title
                    newsDate.text = date
                    newsContent.text = content

                    // Загрузка изображения (если есть)
                    if (imageUrl.isNotEmpty()) {
                        val fullImageUrl = if (imageUrl.startsWith("http")) imageUrl
                        else "https://www.uisi.ru$imageUrl"

                        Glide.with(this@NewsDetailActivity)
                            .load(fullImageUrl)
                            .into(newsImage)
                        newsImage.visibility = View.VISIBLE
                    } else {
                        newsImage.visibility = View.GONE
                    }

                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    newsContent.text = "Не удалось загрузить новость"
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}