package com.example.urtisi.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_SUBJECTS = "subjects"
        private const val TABLE_TASKS = "tasks"
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL("""
                CREATE TABLE $TABLE_SUBJECTS (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL UNIQUE
                )
            """)

            db.execSQL("""
                CREATE TABLE $TABLE_TASKS (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    subject_id INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    is_done INTEGER DEFAULT 0,
                    FOREIGN KEY(subject_id) REFERENCES $TABLE_SUBJECTS(id) ON DELETE CASCADE
                )
            """)
        } catch (e: Exception) {
            Log.e("TaskDatabaseHelper", "Error creating tables", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUBJECTS")
        onCreate(db)
    }

    fun addSubject(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
        }
        return try {
            db.insert(TABLE_SUBJECTS, null, values)
        } catch (e: Exception) {
            Log.e("TaskDatabaseHelper", "Error adding subject", e)
            -1
        }
    }

    fun addTask(subjectId: Long, title: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("subject_id", subjectId)
            put("title", title)
        }
        return try {
            db.insert(TABLE_TASKS, null, values)
        } catch (e: Exception) {
            Log.e("TaskDatabaseHelper", "Error adding task", e)
            -1
        }
    }

    fun getSubjectsWithTasks(): List<SubjectWithTasks> {
        val db = readableDatabase
        val result = mutableListOf<SubjectWithTasks>()

        val query = """
            SELECT s.id as subject_id, s.name, t.id as task_id, t.title, t.is_done
            FROM $TABLE_SUBJECTS s
            LEFT JOIN $TABLE_TASKS t ON s.id = t.subject_id
            ORDER BY s.name, t.title
        """

        db.rawQuery(query, null).use { cursor ->
            var currentSubject: SubjectWithTasks? = null

            while (cursor.moveToNext()) {
                val subjectId = cursor.getLong(cursor.getColumnIndexOrThrow("subject_id"))
                val subjectName = cursor.getString(cursor.getColumnIndexOrThrow("name"))

                if (currentSubject == null || currentSubject.id != subjectId) {
                    currentSubject?.let { result.add(it) }
                    currentSubject = SubjectWithTasks(subjectId, subjectName, mutableListOf())
                }

                if (!cursor.isNull(cursor.getColumnIndexOrThrow("task_id"))) {
                    val taskId = cursor.getLong(cursor.getColumnIndexOrThrow("task_id"))
                    val taskTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                    val isDone = cursor.getInt(cursor.getColumnIndexOrThrow("is_done")) == 1

                    currentSubject.tasks.add(TaskItem(taskId, taskTitle, isDone))
                }
            }
            currentSubject?.let { result.add(it) }
        }

        return result
    }

    fun toggleTaskDone(taskId: Long, isDone: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("is_done", if (isDone) 1 else 0)
        }
        return try {
            db.update(TABLE_TASKS, values, "id = ?", arrayOf(taskId.toString()))
        } catch (e: Exception) {
            Log.e("TaskDatabaseHelper", "Error updating task", e)
            0
        }
    }

    fun deleteSubject(subjectId: Long): Int {
        val db = writableDatabase
        return try {
            db.delete(TABLE_SUBJECTS, "id = ?", arrayOf(subjectId.toString()))
        } catch (e: Exception) {
            Log.e("TaskDatabaseHelper", "Error deleting subject", e)
            0
        }
    }
}

data class SubjectWithTasks(val id: Long, val name: String, val tasks: MutableList<TaskItem>)
data class TaskItem(val id: Long, val title: String, val isDone: Boolean)