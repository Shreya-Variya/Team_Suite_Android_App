package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.data.model.attendance.attendancereport.AttendanceData
import com.example.teamsuite.databinding.ItemAttendanceBinding

class AttendanceAdapter(private var attendanceData: List<AttendanceData>): RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(val binding: ItemAttendanceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AttendanceViewHolder,
        position: Int
    ) {
        val data = attendanceData[position]
        holder.binding.txtDate.text = data.date
        holder.binding.txtStatus.text = data.status
        holder.binding.txtWork.text = formatMillisToTime(data.workTime)
        holder.binding.txtBreak.text = formatMillisToTime(data.breakTime)
    }

    override fun getItemCount(): Int {
        return attendanceData.size
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<AttendanceData>){
        attendanceData = newData
        notifyDataSetChanged()
    }
}