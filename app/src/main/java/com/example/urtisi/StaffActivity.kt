package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val staffList = mutableListOf<Employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)

        recyclerView = findViewById(R.id.staffRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Обработка нажатий на нижнюю панель
        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.footerLayout).apply {
            findViewById<android.widget.ImageView>(R.id.icon1).setOnClickListener {
                // Возврат на главную
                val intent = Intent(this@StaffActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }

            findViewById<android.widget.ImageView>(R.id.icon2).setOnClickListener {
                // Открытие календаря
                Toast.makeText(this@StaffActivity, "Календарь", Toast.LENGTH_SHORT).show()
            }

            findViewById<android.widget.ImageView>(R.id.icon3).setOnClickListener {
                // Открытие карты
                Toast.makeText(this@StaffActivity, "Карта", Toast.LENGTH_SHORT).show()
            }

            findViewById<android.widget.ImageView>(R.id.icon4).setOnClickListener {
                // Открытие настроек
                val intent = Intent(this@StaffActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        loadStaffData()
    }

    private fun loadStaffData() {
        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    StaffParser.parseStaff("https://www.uisi.ru/uisi/general/staff.php")
                }

                Log.d("StaffDebug", "Received ${result.size} items")
                staffList.clear()
                staffList.addAll(result)
                setupAdapter()
            } catch (e: Exception) {
                Log.e("StaffActivity", "Error loading staff", e)
            }
        }
    }

    private fun setupAdapter() {
        recyclerView.adapter = StaffAdapter(staffList) { employee ->
            val intent = Intent(this@StaffActivity, EmployeeDetailActivity::class.java).apply {
                putExtra("employee", employee)
            }
            startActivity(intent)
        }
    }
}