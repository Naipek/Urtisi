package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        findViewById<ImageView>(R.id.icon3).setOnClickListener {
            startActivity(Intent(this, FloorMapActivity::class.java))
        }
        val icon2 = findViewById<ImageView>(R.id.icon2)
        icon2.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        val url = intent.getStringExtra("url") ?: run {
            Toast.makeText(this, "Ошибка: нет URL новости", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadNewsContent(url)
    }

    private fun loadNewsContent(url: String) {
        progressBar.visibility = View.VISIBLE

        // Сначала проверяем кэш
        val cached = NewsDetailCache.load(this, url)
        if (cached != null) {
            displayContent(cached)
            progressBar.visibility = View.GONE
            return
        }

        // Если нет в кэше, загружаем из сети
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url).get()

                val title = doc.select("div.news-detail h3").text()
                val date = doc.select("span.news-date-time").text()
                val content = doc.select("div.news-detail div[align=justify]").text()
                val imageUrlRaw = doc.select("div.news-detail img").attr("src")
                val imageUrl = if (imageUrlRaw.startsWith("http")) imageUrlRaw else "https://www.uisi.ru$imageUrlRaw"

                val news = NewsFullItem(url, title, date, content, imageUrl)

                // Сохраняем в кэш перед отображением
                NewsDetailCache.save(this@NewsDetailActivity, news)

                runOnUiThread {
                    displayContent(news)
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        this@NewsDetailActivity,
                        "Ошибка загрузки. Проверьте подключение к интернету",
                        Toast.LENGTH_SHORT
                    ).show()
                    newsContent.text = "Не удалось загрузить новость"
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun displayContent(news: NewsFullItem) {
        newsTitle.text = news.title
        newsDate.text = news.date
        newsContent.text = news.content

        if (news.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(news.imageUrl)
                .placeholder(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_gallery)) // Стандартная иконка галереи
                .error(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_report_image)) // Стандартная иконка ошибки
                .into(newsImage)
            newsImage.visibility = View.VISIBLE
        } else {
            newsImage.visibility = View.GONE
        }
    }
}