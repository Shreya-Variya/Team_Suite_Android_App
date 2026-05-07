package com.example.teamsuite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teamsuite.data.model.leavepolicy.LeavePolicyData
import com.example.teamsuite.databinding.ItemLeavePolicyBinding

class LeavePolicyAdapter(private var leavePolicyData: List<LeavePolicyData>): RecyclerView.Adapter<LeavePolicyAdapter.LeavePolicyViewHolder>() {

    inner class LeavePolicyViewHolder(val binding: ItemLeavePolicyBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeavePolicyViewHolder {
        val binding = ItemLeavePolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeavePolicyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LeavePolicyViewHolder,
        position: Int
    ) {
        val data = leavePolicyData[position]
        holder.binding.txtLeaveType.text = data.leaveType
        holder.binding.txtMaxLeaveValue.text = data.maxLeavePerYear.toString() + " Days"
        holder.binding.txtConsecutiveValue.text = data.maxConsecutiveLeave.toString() + " Days"
    }

    override fun getItemCount(): Int {
        return leavePolicyData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<LeavePolicyData>){
        leavePolicyData = newData
        notifyDataSetChanged()
    }
}