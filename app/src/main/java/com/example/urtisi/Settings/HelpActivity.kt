package com.example.urtisi.Settings

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_help)


        // Обработчики для нижней панели
        val icon1 = findViewById<ImageView>(R.id.icon1)
        icon1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val icon4 = findViewById<ImageView>(R.id.icon4)
        icon4.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}