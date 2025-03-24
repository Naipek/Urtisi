package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity

class EmployeesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_employees)


        // Обработчики для нижней панели
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}


