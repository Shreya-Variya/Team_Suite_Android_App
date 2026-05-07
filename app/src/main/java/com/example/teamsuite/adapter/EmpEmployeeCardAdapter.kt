package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.data.model.login.UserData
import com.example.teamsuite.databinding.EmpEmployeeCardLayoutBinding

class EmpEmployeeCardAdapter(private var employeeData: List<UserData>): RecyclerView.Adapter<EmpEmployeeCardAdapter.EmpEmployeeViewHolder>() {

    inner class EmpEmployeeViewHolder(val binding: EmpEmployeeCardLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmpEmployeeCardAdapter.EmpEmployeeViewHolder {
        val binding = EmpEmployeeCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmpEmployeeViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: EmpEmployeeCardAdapter.EmpEmployeeViewHolder,
        position: Int
    ) {
        val employee = employeeData[position]
        holder.binding.txtEmployeeName.text = employee.employeeName
        holder.binding.txtJobRole.text = employee.jobRole.role
        holder.binding.txtDepartment.text = employee.department.name
        holder.binding.txtEmail.text = employee.email
        holder.binding.txtMobileNo.text = employee.mobileNo
        holder.binding.txtExp.text = "${employee.experience} years"
    }

    override fun getItemCount(): Int {
        return employeeData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<UserData>){
        employeeData = newData
        notifyDataSetChanged()
    }
}