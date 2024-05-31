package com.mfo.turnosmedicos.ui.scheduleAppointment.appointment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.turnosmedicos.R
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse

class AppointmentAdapter(private var hourList: List<String> = mutableListOf(), private val onItemSelected:(String) -> Unit): RecyclerView.Adapter<AppointmentViewHolder>() {
    fun updateList(list: List<String>) {
        hourList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        return AppointmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return hourList.size
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(hourList[position], onItemSelected)
    }
}