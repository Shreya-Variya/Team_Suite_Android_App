package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.R
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceByDateData
import com.example.teamsuite.databinding.CardEmpAttendanceBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class EmpAttendanceAdapter(private var attendanceData: List<AttendanceByDateData>): RecyclerView.Adapter<EmpAttendanceAdapter.EmpAttendanceViewHolder>() {

    inner class EmpAttendanceViewHolder(val binding: CardEmpAttendanceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmpAttendanceAdapter.EmpAttendanceViewHolder {
        val binding = CardEmpAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmpAttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EmpAttendanceAdapter.EmpAttendanceViewHolder,
        position: Int
    ) {
        val data = attendanceData[position]
        val date = getDate(data.date)
        holder.binding.txtEmpId.text = data.employeeId
        holder.binding.txtName.text = data.name
        holder.binding.txtDate.text = date
        holder.binding.txtStatus.text = data.status
        holder.binding.txtWork.text = formatMillisToTime(data.workTime)
        holder.binding.txtBreak.text = formatMillisToTime(data.breakTime)
        if (data.status == "Present"){
            holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_present)
        }
        else if (data.status == "Absent"){
            holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_absent)
        }
        else if (data.status == "Weekend"){
            holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_weekend)
        }
        else if (data.status == "Leave"){
            holder.binding.txtStatus.setBackgroundResource(R.drawable.bg_status_leave)
        }
    }

    override fun getItemCount(): Int {
        return  attendanceData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<AttendanceByDateData>){
        attendanceData = newData
        notifyDataSetChanged()
    }

    private fun  getDate(date: String): String{
        return try {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            val parsedDate = inputFormat.parse(date)

            val calendar = Calendar.getInstance()
            calendar.time = parsedDate!!

            calendar.add(Calendar.DAY_OF_MONTH, 1)

            outputFormat.format(calendar.time)
        } catch (e: Exception) {
            ""
        }
    }

    //Function that convert the Long to Time
    @SuppressLint("DefaultLocale")
    private fun formatMillisToTime(ms: Long): String {
        val seconds = ms / 1000
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return when {
            hours > 0 -> String.format("%02dh %02dm", hours, minutes)
            minutes > 0 -> String.format("%02dm %02ds", minutes, secs)
            else -> String.format("%02ds", secs)
        }
    }
}