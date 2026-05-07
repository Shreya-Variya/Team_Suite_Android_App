package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.R
import com.example.teamsuite.data.model.leave.GetLeaveReportData
import com.example.teamsuite.databinding.ItemEmpLeaveBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class EmployeeLeaveAdapter(private var leaveData: List<GetLeaveReportData>): RecyclerView.Adapter<EmployeeLeaveAdapter.EmployeeLeaveViewHolder>() {

    inner class EmployeeLeaveViewHolder(val binding: ItemEmpLeaveBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeLeaveViewHolder {
        val binding = ItemEmpLeaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeLeaveViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EmployeeLeaveViewHolder,
        position: Int
    ) {
        val leave = leaveData[position]
        val startDate = getDate(leave.startDate)
        val endDate = getDate(leave.endDate)
        holder.binding.txtLeavePolicyName.text = leave.leaveType
        holder.binding.txtStartDate.text = startDate
        holder.binding.txtEndDate.text = endDate
        holder.binding.txtReason.text = leave.reason
        holder.binding.statusChip.text = leave.status
        if (leave.status == "Approved"){
            holder.binding.statusChip.chipBackgroundColor =  ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.green)
            )
        }
        else if (leave.status == "Rejected"){
            holder.binding.statusChip.chipBackgroundColor =  ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.red)
            )
        }
        else if (leave.status == "Pending"){
            holder.binding.statusChip.chipBackgroundColor =  ColorStateList.valueOf(
                ContextCompat.getColor(holder.itemView.context, R.color.blue)
            )
        }
    }

    override fun getItemCount(): Int {
        return leaveData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<GetLeaveReportData>){
        leaveData = newData
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