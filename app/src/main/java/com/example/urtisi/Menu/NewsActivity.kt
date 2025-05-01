package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urtisi.FloorMapActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.NewsAdapter
import com.example.urtisi.NewsCache
import com.example.urtisi.NewsParser
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_news)

        // Обработчики для нижней панели
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.icon3).setOnClickListener {
            startActivity(Intent(this, FloorMapActivity::class.java))
        }
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        recyclerView = findViewById(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadNews()
    }

    private fun loadNews() {
        lifecycleScope.launch {
            try {
                // Пытаемся загрузить свежие новости
                val freshNews = NewsParser.parseNews()
                if (freshNews.isNotEmpty()) {
                    NewsCache.save(this@NewsActivity, freshNews)
                    recyclerView.adapter = NewsAdapter(freshNews)
                } else {
                    throw Exception("Пустой список новостей")
                }
            } catch (e: Exception) {
                // Если не получилось, загружаем из кэша
                val cachedNews = NewsCache.load(this@NewsActivity)
                if (cachedNews.isNotEmpty()) {
                    Toast.makeText(
                        this@NewsActivity,
                        "Показываем кэшированные новости",
                        Toast.LENGTH_SHORT
                    ).show()
                    recyclerView.adapter = NewsAdapter(cachedNews)
                } else {
                    Toast.makeText(
                        this@NewsActivity,
                        "Нет подключения к интернету и кэшированных данных",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}