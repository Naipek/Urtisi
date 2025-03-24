package com.example.urtisi.Settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity

class ThemeSelectionActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_theme_selection)

        // Инициализируем SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)

        // Устанавливаем сохранённую тему при запуске
        val savedTheme = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedTheme)

        // Обработчики для карточек выбора темы
        val cardLight = findViewById<CardView>(R.id.cardLight)
        cardLight.setOnClickListener {
            saveTheme(AppCompatDelegate.MODE_NIGHT_NO) // Светлая тема
        }
        val cardDark = findViewById<CardView>(R.id.cardDark)
        cardDark.setOnClickListener {
            saveTheme(AppCompatDelegate.MODE_NIGHT_YES) // Тёмная тема
        }
        val cardSystem = findViewById<CardView>(R.id.cardSystem)
        cardSystem.setOnClickListener {
            saveTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) // Системная тема
        }

        // Обработчики для нижней панели
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun saveTheme(themeMode: Int) {
        // Сохраняем выбранную тему
        sharedPreferences.edit().putInt("theme_mode", themeMode).apply()
        AppCompatDelegate.setDefaultNightMode(themeMode)
        recreate() // Перезагрузка активности для применения темы
    }
}
