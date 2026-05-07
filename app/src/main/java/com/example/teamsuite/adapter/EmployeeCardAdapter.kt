package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.databinding.EmployeeCardLayoutBinding
import com.example.teamsuite.data.model.login.UserData

class EmployeeCardAdapter(private var employeeData: List<UserData>, private val onEditClick: (UserData) -> Unit, private val onDeleteClick: (UserData) -> Unit): RecyclerView.Adapter<EmployeeCardAdapter.EmployeeViewHolder>() {

    inner class EmployeeViewHolder(val binding: EmployeeCardLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeViewHolder {
        val binding = EmployeeCardLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return EmployeeViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: EmployeeViewHolder,
        position: Int
    ) {
        val employee = employeeData[position]
        holder.binding.txtEmpID.text = employee.employeeId
        holder.binding.txtEmployeeName.text = employee.employeeName
        holder.binding.txtJobRole.text = employee.jobRole.role
        holder.binding.txtDepartment.text = employee.department.name
        holder.binding.txtEmail.text = employee.email
        holder.binding.txtMobileNo.text = employee.mobileNo
        holder.binding.txtExp.text = "${employee.experience} years"

        holder.binding.btnEdit.setOnClickListener { onEditClick(employee) }
        holder.binding.btnDelete.setOnClickListener { onDeleteClick(employee) }
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