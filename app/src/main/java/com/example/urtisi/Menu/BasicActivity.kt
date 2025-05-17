package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.urtisi.FloorMapActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.ScheduleActivity
import com.example.urtisi.SettingsActivity

class BasicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_basic)

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
        val icon2 = findViewById<ImageView>(R.id.icon2)
        icon2.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }
    }

}