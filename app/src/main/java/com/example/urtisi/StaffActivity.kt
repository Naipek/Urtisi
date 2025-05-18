package com.example.urtisi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private val staffList = mutableListOf<Employee>()
    private val filteredList = mutableListOf<Employee>()
    private lateinit var adapter: StaffAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)

        recyclerView = findViewById(R.id.staffRecyclerView)
        searchView = findViewById(R.id.searchView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализация адаптера
        adapter = StaffAdapter(filteredList) { employee ->
            val intent = Intent(this, EmployeeDetailActivity::class.java).apply {
                putExtra("employee", employee)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Настройка поиска
        setupSearchView()

        // Обработка нижней панели
        val footerLayout = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.footerLayout)
        footerLayout?.let {
            findViewById<android.widget.ImageView>(R.id.icon1)?.setOnClickListener {
                val intent = Intent(this@StaffActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }

            val icon2 = findViewById<ImageView>(R.id.icon2)
            icon2.setOnClickListener {
                val intent = Intent(this, ScheduleActivity::class.java)
                startActivity(intent)
            }

            val icon3 = findViewById<ImageView>(R.id.icon3)
            icon3.setOnClickListener {
                val intent = Intent(this, FloorMapActivity::class.java)
                startActivity(intent)
            }

            findViewById<android.widget.ImageView>(R.id.icon4)?.setOnClickListener {
                val intent = Intent(this@StaffActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        } ?: run {
            Log.w("StaffActivity", "Footer layout not found")
        }

        loadStaffData()
    }

    private fun setupSearchView() {
        searchView.apply {
            // Настройка внешнего вида (без прямого доступа к SearchAutoComplete)
            try {
                // Альтернативный способ изменения цвета текста
                val searchTextId = resources.getIdentifier("search_src_text", "id", packageName)
                val searchText = findViewById<android.widget.AutoCompleteTextView>(searchTextId)
                searchText?.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.text_dark))
                    setHintTextColor(ContextCompat.getColor(context, R.color.hint_color))
                    hint = "Поиск сотрудников"
                }
            } catch (e: Exception) {
                Log.e("StaffActivity", "Error styling SearchView", e)
            }

            // Обработка поиска
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterList(newText)
                    return true
                }
            })

            queryHint = "Поиск сотрудников"
            isIconifiedByDefault = false
        }
    }

    private fun filterList(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(staffList)
        } else {
            val lowerCaseQuery = query.lowercase()
            staffList.forEach { employee ->
                if (employee.name.lowercase().contains(lowerCaseQuery) ||
                    employee.position.lowercase().contains(lowerCaseQuery)) {
                    filteredList.add(employee)
                }
            }
        }
        adapter.notifyDataSetChanged()
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
                filterList("")
            } catch (e: Exception) {
                Log.e("StaffActivity", "Error loading staff", e)
                Toast.makeText(
                    this@StaffActivity,
                    "Ошибка загрузки данных",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}