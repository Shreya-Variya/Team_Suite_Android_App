package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.data.model.attendance.attendancereport.GetEmployeeAttendanceData
import com.example.teamsuite.databinding.ItemEmpAttendanceBinding

class EmployeeAttendanceAdapter(private var attendanceData: List<GetEmployeeAttendanceData>): RecyclerView.Adapter<EmployeeAttendanceAdapter.EmployeeAttendanceViewHolder>() {
    inner class EmployeeAttendanceViewHolder(val binding: ItemEmpAttendanceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeAttendanceAdapter.EmployeeAttendanceViewHolder {
        val binding = ItemEmpAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EmployeeAttendanceAdapter.EmployeeAttendanceViewHolder,
        position: Int
    ) {
        val data = attendanceData[position]
        holder.binding.txtEmpId.text = data.employeeId
        holder.binding.txtName.text = data.name
        holder.binding.txtDate.text = data.date
        holder.binding.txtStatus.text = data.status
    }

    override fun getItemCount(): Int {
        return attendanceData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<GetEmployeeAttendanceData>){
        attendanceData = newData
        notifyDataSetChanged()
    }
}