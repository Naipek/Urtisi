package com.example.urtisi.Menu.Education

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity

class EducationSpo2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.education_spo2)


        // Обработчики для нижней панели
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}