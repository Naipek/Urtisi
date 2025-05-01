package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urtisi.NewsAdapter
import com.example.urtisi.NewsParser
import com.example.urtisi.R
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.urtisi.FloorMapActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.SettingsActivity

class NewsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_news)

        // Обработчики для нижней панели
        val icon1 = findViewById<ImageView>(R.id.icon1)
        icon1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val icon3 = findViewById<ImageView>(R.id.icon3)
        icon3.setOnClickListener {
            val intent = Intent(this, FloorMapActivity::class.java)
            startActivity(intent)
        }
        val icon4 = findViewById<ImageView>(R.id.icon4)
        icon4.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val news = NewsParser.parseNews()
                Log.d("NewsActivity", "Received ${news.size} news items")

                if (news.isEmpty()) {
                    Toast.makeText(
                        this@NewsActivity,
                        "Новости не найдены",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    recyclerView.adapter = NewsAdapter(news)
                }
            } catch (e: Exception) {
                Log.e("NewsActivity", "Error loading news", e)
                Toast.makeText(
                    this@NewsActivity,
                    "Ошибка загрузки новостей",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}