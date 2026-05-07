package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.R
import com.example.teamsuite.data.model.leave.AllEmployeeLeaveReportData
import com.example.teamsuite.databinding.ItemAdminLeaveBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AdminLeaveAdapter(private var leaveReport: List<AllEmployeeLeaveReportData>, private val onAcceptClick: (AllEmployeeLeaveReportData)-> Unit, private val onRejectClick: (AllEmployeeLeaveReportData) -> Unit): RecyclerView.Adapter<AdminLeaveAdapter.AdminLeaveViewHolder>() {

    inner class AdminLeaveViewHolder(val binding: ItemAdminLeaveBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminLeaveViewHolder {
        val binding = ItemAdminLeaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminLeaveViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AdminLeaveViewHolder,
        position: Int
    ) {
        val leave = leaveReport[position]
        val startDate = getDate(leave.startDate)
        val endDate = getDate(leave.endDate)
        holder.binding.txtEmployeeName.text = leave.employeeName
        holder.binding.txtLeavePolicy.text = leave.leavePolicyName
        holder.binding.txtStartDate.text = startDate
        holder.binding.txtEndDate.text = endDate
        holder.binding.statusChip.text = leave.leaveStatus
        if (leave.leaveStatus == "Approved"){
            holder.binding.statusChip.chipBackgroundColor =  ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.green)
            )
        }
        else if (leave.leaveStatus == "Rejected"){
            holder.binding.statusChip.chipBackgroundColor =  ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.red)
            )
        }
        else if (leave.leaveStatus == "Pending"){
            holder.binding.statusChip.chipBackgroundColor =  ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.blue)
            )
            holder.binding.btnAccept.visibility = View.VISIBLE
            holder.binding.btnReject.visibility = View.VISIBLE

            holder.binding.btnAccept.setOnClickListener { onAcceptClick(leave) }
            holder.binding.btnReject.setOnClickListener { onRejectClick(leave) }
        }
    }

    override fun getItemCount(): Int {
        return leaveReport.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<AllEmployeeLeaveReportData>){
        leaveReport = newData
        notifyDataSetChanged()
    }

    private fun  getDate(date: String): String{
        return try {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            val parsedDate = inputFormat.parse(date)

            val calendar = Calendar.getInstance()
            calendar.time = parsedDate!!
//            calendar.add(Calendar.DAY_OF_MONTH, 1)

            outputFormat.format(calendar.time)
        } catch (e: Exception) {
            ""
        }
    }
}