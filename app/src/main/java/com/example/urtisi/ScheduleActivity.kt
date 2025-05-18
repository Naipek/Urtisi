package com.example.urtisi

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urtisi.adapters.ScheduleAdapter
import com.example.urtisi.utils.ScheduleReader
import com.example.urtisi.data.Schedule
import android.view.LayoutInflater
import android.app.AlertDialog
import android.view.View
import android.widget.RadioGroup
import android.widget.RadioButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Context
import android.content.Intent

class ScheduleActivity : AppCompatActivity(), EducationFilterDialog.FilterSelectionListener {
    private lateinit var weekRangeText: TextView
    private lateinit var prevWeekButton: ImageButton
    private lateinit var nextWeekButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter
    private lateinit var filterFab: FloatingActionButton
    private lateinit var selectedFilterTextView: TextView
    private lateinit var noScheduleText: TextView
    private lateinit var headerTitle: TextView
    
    private val TAG = "ScheduleActivity"
    private var currentWeekIndex = 0
    private var dateRanges = listOf<String>()
    private var allSchedules = listOf<Schedule>()
    private var currentGroup: String? = null

    companion object {
        private const val PREFS_NAME = "SchedulePrefs"
        private const val KEY_SELECTED_GROUP = "selected_group"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

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


        // Загружаем сохраненную группу
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        currentGroup = prefs.getString(KEY_SELECTED_GROUP, null)
        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

        // Инициализация views
        weekRangeText = findViewById(R.id.weekRangeText)
        prevWeekButton = findViewById(R.id.prevWeekButton)
        nextWeekButton = findViewById(R.id.nextWeekButton)
        recyclerView = findViewById(R.id.scheduleRecyclerView)
        filterFab = findViewById(R.id.filterFab)
        selectedFilterTextView = findViewById(R.id.selectedFilterTextView)
        noScheduleText = findViewById(R.id.noScheduleText)
        headerTitle = findViewById(R.id.headerTitle)

        // Устанавливаем заголовок
        headerTitle.text = "Расписание"

        // Настраиваем RecyclerView
        adapter = ScheduleAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Настраиваем обработчики кнопок
        prevWeekButton.setOnClickListener {
            if (currentWeekIndex > 0) {
                currentWeekIndex--
                updateWeekDisplay()
            }
        }

        nextWeekButton.setOnClickListener {
            if (currentWeekIndex < dateRanges.size - 1) {
                currentWeekIndex++
                updateWeekDisplay()
            }
        }

        filterFab.setOnClickListener {
            showFilterDialog()
        }

        // Restore saved filter
        val sharedPreferences = getSharedPreferences("EducationFilter", MODE_PRIVATE)
        val savedEducationType = sharedPreferences.getString("education_type", "")
        val savedCourse = sharedPreferences.getInt("course", -1)
        val savedGroup = sharedPreferences.getString("selected_group", "")
        
        if (!savedEducationType.isNullOrEmpty() && savedCourse != -1 && !savedGroup.isNullOrEmpty()) {
            updateFilterDisplay(savedEducationType, savedCourse, savedGroup)
        }

        if (currentGroup == null) {
            // Показываем текст с подсказкой выбрать группу
            showNoScheduleState()
            if (isFirstLaunch) {
                // Сохраняем, что это уже не первый запуск
                prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
                // Автоматически открываем диалог выбора фильтра
                showFilterDialog()
            }
        } else {
            loadSchedule()
        }
    }

    private fun showNoScheduleState() {
        noScheduleText.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        weekRangeText.visibility = View.GONE
        prevWeekButton.visibility = View.GONE
        nextWeekButton.visibility = View.GONE
    }

    private fun showScheduleState() {
        noScheduleText.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        weekRangeText.visibility = View.VISIBLE
        prevWeekButton.visibility = View.VISIBLE
        nextWeekButton.visibility = View.VISIBLE
    }

    private fun showGroupSelectionDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_group_selector, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.groupRadioGroup)

        // Получаем список доступных групп из assets
        val groups = assets.list("schedules")
            ?.filter { it.endsWith(".xlsx") }
            ?.map { it.removeSuffix(".xlsx") }
            ?.sorted() // Сортируем группы по алфавиту
            ?: listOf()

        // Создаем радиокнопки для каждой группы
        groups.forEach { group ->
            val radioButton = RadioButton(this).apply {
                text = group
                id = View.generateViewId()
                isChecked = group == currentGroup
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }
            radioGroup.addView(radioButton)
        }

        AlertDialog.Builder(this, R.style.RoundedDialog)
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                val selectedId = radioGroup.checkedRadioButtonId
                val selectedRadioButton = dialogView.findViewById<RadioButton>(selectedId)
                val selectedGroup = selectedRadioButton?.text?.toString()
                if (selectedGroup != null && selectedGroup != currentGroup) {
                    currentGroup = selectedGroup
                    // Сохраняем выбранную группу
                    getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putString(KEY_SELECTED_GROUP, currentGroup)
                        .apply()
                    loadSchedule()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun loadSchedule() {
        if (currentGroup == null) {
            showNoScheduleState()
            return
        }

        try {
            val fileName = "$currentGroup.xlsx"
            Log.d(TAG, "Пытаемся загрузить файл: $fileName")
            
            allSchedules = ScheduleReader.readSchedule(this, fileName)
            
            if (allSchedules.isEmpty()) {
                Log.e(TAG, "Расписание пустое")
                Toast.makeText(this, "Не удалось загрузить расписание", Toast.LENGTH_LONG).show()
                return
            }

            // Получаем уникальные диапазоны дат и сортируем их по дате
            dateRanges = allSchedules
                .map { it.dateRange }
                .distinct()
                .filter { it.isNotEmpty() && it.contains("-") }
                .sortedBy { dateRange ->
                    try {
                        val cleanDateRange = dateRange.replace("..", ".").replace(".-", "-")
                        val startDate = cleanDateRange.split("-")[0].trim()
                        if (startDate.contains(".")) {
                            val (day, month) = startDate.split(".").filter { it.isNotEmpty() }.map { it.toInt() }
                            month * 100 + day
                        } else {
                            0
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Ошибка при парсинге даты: $dateRange", e)
                        0
                    }
                }
            
            if (dateRanges.isEmpty()) {
                Log.e(TAG, "Не найдено корректных диапазонов дат")
                Toast.makeText(this, "Ошибка в формате дат расписания", Toast.LENGTH_LONG).show()
                return
            }
            
            Log.d(TAG, "Найденные недели: ${dateRanges.joinToString()}")
            
            // Отображаем первую неделю
            currentWeekIndex = 0
            updateWeekDisplay()
            showScheduleState()

        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при загрузке расписания", e)
            Toast.makeText(this, "Ошибка при загрузке расписания", Toast.LENGTH_LONG).show()
            showNoScheduleState()
        }
    }

    private fun updateWeekDisplay() {
        // Обновляем текст с диапазоном дат
        weekRangeText.text = dateRanges[currentWeekIndex]

        // Обновляем видимость кнопок
        prevWeekButton.isEnabled = currentWeekIndex > 0
        prevWeekButton.alpha = if (currentWeekIndex > 0) 1.0f else 0.5f

        nextWeekButton.isEnabled = currentWeekIndex < dateRanges.size - 1
        nextWeekButton.alpha = if (currentWeekIndex < dateRanges.size - 1) 1.0f else 0.5f

        // Обновляем расписание
        val weekSchedule = allSchedules.filter { it.dateRange == dateRanges[currentWeekIndex] }
        Log.d(TAG, "Отображаем расписание для ${dateRanges[currentWeekIndex]}, найдено ${weekSchedule.size} пар")
        displaySchedule(weekSchedule)
    }

    private fun displaySchedule(schedules: List<Schedule>) {
        adapter.updateSchedules(schedules)
    }

    private fun showFilterDialog() {
        val dialog = EducationFilterDialog()
        
        val groups = mapOf(
            "ВО" to mapOf(
                1 to listOf("ИТ-41", "ИТ-42", "ИТ-43", "ПЕ-41б", "ПЕ-42б", "ТЕ-41", "ТЕ-42"),
                2 to listOf("ИТ-31", "ИТ-32", "ПЕ-31б", "ПЕ-32б", "ТЕ-31", "ТЕ-32"),
                3 to listOf("ИТ-21б", "ИТ-22б", "ПЕ-21б", "ПЕ-22б", "ТЕ-21б", "ТЕ-22б"),
                4 to listOf("ИТ-11б", "МС-11б", "ОЕ-11б", "ПЕ-11б", "ПЕ-12б", "ТЕ-11б")
            ),
            "СПО" to mapOf(
                1 to listOf("421", "422", "423", "481", "482", "483", "484", "485", "486", "487"),
                2 to listOf("321", "322", "323", "383", "384", "385", "386"),
                3 to listOf("221", "222", "223"),
                4 to listOf("121", "122", "183", "184", "185")
            )
        )
        
        dialog.updateGroups(groups)
        dialog.show(supportFragmentManager, EducationFilterDialog.TAG)
    }

    override fun onFilterSelected(educationType: String, course: Int, group: String) {
        updateFilterDisplay(educationType, course, group)
        currentGroup = group
        // Сохраняем выбранную группу
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SELECTED_GROUP, group)
            .apply()
        loadSchedule()
    }

    private fun updateFilterDisplay(educationType: String, course: Int, group: String) {
        selectedFilterTextView.text = group
        selectedFilterTextView.visibility = View.VISIBLE
    }
}