package com.mfo.turnosmedicos.ui.historyAppointments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.turnosmedicos.R
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse

class HistoryAdapter(private var appointmentList: List<AppointmentResponse> = emptyList()): RecyclerView.Adapter<HistoryViewHolder>() {
    fun updateList(list: List<AppointmentResponse>) {
        appointmentList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(appointmentList[position])
    }
}