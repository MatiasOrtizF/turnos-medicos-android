package com.mfo.turnosmedicos.ui.scheduleAppointment.appointment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mfo.turnosmedicos.databinding.ItemAppointmentBinding

class AppointmentViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemAppointmentBinding.bind(view)

    fun bind(hour: String, onItemSelected: (String) -> Unit) {
        binding.tvHour.text = hour.substring(0,5)
        itemView.setOnClickListener {
            onItemSelected(hour)
        }
    }
}