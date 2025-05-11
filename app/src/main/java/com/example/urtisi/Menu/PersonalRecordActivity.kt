package com.example.urtisi.Menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urtisi.FloorMapActivity
import com.example.urtisi.MainActivity
import com.example.urtisi.R
import com.example.urtisi.SettingsActivity
import com.example.urtisi.data.TaskDatabaseHelper
import com.example.urtisi.data.SubjectWithTasks

class PersonalRecordActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SubjectAdapter
    private lateinit var db: TaskDatabaseHelper
    private lateinit var addButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_list)

        db = TaskDatabaseHelper(this)
        recyclerView = findViewById(R.id.subjectRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        addButton = findViewById(R.id.addSubjectButton)

        adapter = SubjectAdapter(db.getSubjectsWithTasks(),
            onTaskChecked = { taskId, isChecked ->
                db.toggleTaskDone(taskId, isChecked)
            },
            onSubjectLongClick = { subjectId ->
                showDeleteSubjectDialog(subjectId)
                true
            }
        )

        recyclerView.adapter = adapter

        // Обработчик для кнопки добавления
        addButton.setOnClickListener {
            showAddSubjectDialog()
        }

        // Подключение нижней панели
        findViewById<ImageView>(R.id.icon1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<ImageView>(R.id.icon3).setOnClickListener {
            startActivity(Intent(this, FloorMapActivity::class.java))
        }

        findViewById<ImageView>(R.id.icon4).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun showAddSubjectDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_subject, null)
        val editTextSubject = dialogView.findViewById<EditText>(R.id.editSubjectName)
        val editPracticesCount = dialogView.findViewById<EditText>(R.id.editPracticesCount)
        val editLabsCount = dialogView.findViewById<EditText>(R.id.editLabsCount)
        val editOtherTasks = dialogView.findViewById<EditText>(R.id.editOtherTasks)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Добавить предмет")
            .setView(dialogView)
            .setPositiveButton("Сохранить", null)
            .setNegativeButton("Отмена", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val subjectName = editTextSubject.text.toString().trim()
                if (subjectName.isEmpty()) {
                    editTextSubject.error = "Введите название предмета"
                    return@setOnClickListener
                }

                val practicesCount = try {
                    editPracticesCount.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }

                val labsCount = try {
                    editLabsCount.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }

                val otherTasks = editOtherTasks.text.toString()
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                if (practicesCount == 0 && labsCount == 0 && otherTasks.isEmpty()) {
                    editOtherTasks.error = "Введите хотя бы один тип заданий"
                    return@setOnClickListener
                }

                val subjectId = db.addSubject(subjectName)
                if (subjectId != -1L) {
                    // Добавляем практики с ведущими нулями для правильной сортировки
                    for (i in 1..practicesCount) {
                        val practiceNumber = String.format("%02d", i) // Добавляем ведущий ноль для чисел < 10
                        db.addTask(subjectId, "Практика $practiceNumber")
                    }

                    // Добавляем лабораторные работы с ведущими нулями
                    for (i in 1..labsCount) {
                        val labNumber = String.format("%02d", i) // Добавляем ведущий ноль для чисел < 10
                        db.addTask(subjectId, "Лаб. работа $labNumber")
                    }

                    // Добавляем другие задания
                    otherTasks.forEach { task ->
                        db.addTask(subjectId, task)
                    }

                    refreshData()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun showDeleteSubjectDialog(subjectId: Long) {
        AlertDialog.Builder(this)
            .setTitle("Удалить предмет")
            .setMessage("Вы уверены, что хотите удалить этот предмет?")
            .setPositiveButton("Удалить") { _, _ ->
                db.deleteSubject(subjectId)
                refreshData()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun refreshData() {
        adapter.updateSubjects(db.getSubjectsWithTasks())
    }

    inner class SubjectAdapter(
        private var subjects: List<SubjectWithTasks>,
        private val onTaskChecked: (Long, Boolean) -> Unit,
        private val onSubjectLongClick: (Long) -> Boolean
    ) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

        inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val subjectTitle: TextView = itemView.findViewById(R.id.subjectTitle)
            val taskContainer: LinearLayout = itemView.findViewById(R.id.taskList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subject, parent, false)
            return SubjectViewHolder(view)
        }

        override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
            val subject = subjects[position]

            holder.subjectTitle.text = subject.name
            holder.taskContainer.removeAllViews()

            subject.tasks.forEach { task ->
                val taskView = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.item_task, holder.taskContainer, false)

                val checkBox = taskView.findViewById<CheckBox>(R.id.taskCheckBox)
                checkBox.text = task.title
                checkBox.isChecked = task.isDone
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskChecked(task.id, isChecked)
                }

                holder.taskContainer.addView(taskView)
            }

            holder.itemView.setOnLongClickListener {
                onSubjectLongClick(subject.id)
            }
        }

        override fun getItemCount() = subjects.size

        fun updateSubjects(newSubjects: List<SubjectWithTasks>) {
            subjects = newSubjects
            notifyDataSetChanged()
        }
    }
}