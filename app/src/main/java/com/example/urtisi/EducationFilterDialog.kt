package com.example.urtisi

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class EducationFilterDialog : DialogFragment() {
    interface FilterSelectionListener {
        fun onFilterSelected(educationType: String, course: Int, group: String)
    }

    private var listener: FilterSelectionListener? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var groupSpinner: Spinner
    private lateinit var educationLevelGroup: RadioGroup
    private lateinit var courseGroup: RadioGroup

    // Временное хранилище групп (позже вы предоставите актуальные данные)
    private val groupsMap = mutableMapOf<String, MutableMap<Int, List<String>>>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as FilterSelectionListener
            sharedPreferences = context.getSharedPreferences("EducationFilter", Context.MODE_PRIVATE)
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement FilterSelectionListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_education_filter, null)

        educationLevelGroup = view.findViewById(R.id.educationLevelGroup)
        courseGroup = view.findViewById(R.id.courseGroup)
        groupSpinner = view.findViewById(R.id.groupSpinner)

        // Restore saved selections
        val savedEducationType = sharedPreferences.getString("education_type", "")
        val savedCourse = sharedPreferences.getInt("course", -1)
        val savedGroup = sharedPreferences.getString("selected_group", "")

        // Set saved education type
        if (!savedEducationType.isNullOrEmpty()) {
            val radioButton = when (savedEducationType) {
                "СПО" -> view.findViewById<RadioButton>(R.id.radioSPO)
                "ВО" -> view.findViewById<RadioButton>(R.id.radioVO)
                else -> null
            }
            radioButton?.isChecked = true
        }

        // Set saved course
        if (savedCourse != -1) {
            val radioButton = when (savedCourse) {
                1 -> view.findViewById<RadioButton>(R.id.radio1Course)
                2 -> view.findViewById<RadioButton>(R.id.radio2Course)
                3 -> view.findViewById<RadioButton>(R.id.radio3Course)
                4 -> view.findViewById<RadioButton>(R.id.radio4Course)
                else -> null
            }
            radioButton?.isChecked = true
        }

        // Setup listeners for education type and course selection
        educationLevelGroup.setOnCheckedChangeListener { _, _ ->
            updateGroupSpinner()
        }

        courseGroup.setOnCheckedChangeListener { _, _ ->
            updateGroupSpinner()
        }

        builder.setView(view)
            .setPositiveButton("ОК") { _, _ ->
                val selectedEducationType = when (educationLevelGroup.checkedRadioButtonId) {
                    R.id.radioSPO -> "СПО"
                    R.id.radioVO -> "ВО"
                    else -> return@setPositiveButton
                }

                val selectedCourse = when (courseGroup.checkedRadioButtonId) {
                    R.id.radio1Course -> 1
                    R.id.radio2Course -> 2
                    R.id.radio3Course -> 3
                    R.id.radio4Course -> 4
                    else -> return@setPositiveButton
                }

                val selectedGroup = groupSpinner.selectedItem?.toString() ?: return@setPositiveButton

                // Save selections
                sharedPreferences.edit().apply {
                    putString("education_type", selectedEducationType)
                    putInt("course", selectedCourse)
                    putString("selected_group", selectedGroup)
                    apply()
                }

                listener?.onFilterSelected(selectedEducationType, selectedCourse, selectedGroup)
            }
            .setNegativeButton("Отмена", null)

        // Initial update of the spinner
        updateGroupSpinner()

        return builder.create()
    }

    private fun updateGroupSpinner() {
        val selectedEducationType = when (educationLevelGroup.checkedRadioButtonId) {
            R.id.radioSPO -> "СПО"
            R.id.radioVO -> "ВО"
            else -> return
        }

        val selectedCourse = when (courseGroup.checkedRadioButtonId) {
            R.id.radio1Course -> 1
            R.id.radio2Course -> 2
            R.id.radio3Course -> 3
            R.id.radio4Course -> 4
            else -> return
        }

        // Получаем список групп для выбранного уровня образования и курса
        val groups = groupsMap
            .getOrDefault(selectedEducationType, mutableMapOf())
            .getOrDefault(selectedCourse, emptyList())

        // Обновляем спиннер
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            groups
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        
        groupSpinner.adapter = adapter

        // Восстанавливаем сохраненную группу, если она есть в текущем списке
        val savedGroup = sharedPreferences.getString("selected_group", "")
        if (!savedGroup.isNullOrEmpty() && groups.contains(savedGroup)) {
            val position = groups.indexOf(savedGroup)
            groupSpinner.setSelection(position)
        }
    }

    fun updateGroups(groups: Map<String, Map<Int, List<String>>>) {
        groupsMap.clear()
        groups.forEach { (educationType, coursesMap) ->
            groupsMap[educationType] = coursesMap.toMutableMap()
        }
        if (this::groupSpinner.isInitialized) {
            updateGroupSpinner()
        }
    }

    companion object {
        const val TAG = "EducationFilterDialog"
    }
} 