package com.example.urtisi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StaffAdapter(
    private var staffList: List<Employee>,
    private val onItemClick: (Employee) -> Unit
) : RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {

    inner class StaffViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val positionTextView: TextView = view.findViewById(R.id.positionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return StaffViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val employee = staffList[position]
        holder.nameTextView.text = employee.name
        holder.positionTextView.text = employee.position

        holder.itemView.setOnClickListener {
            onItemClick(employee)
        }
    }

    override fun getItemCount(): Int = staffList.size
}
