package com.mfo.turnosmedicos.ui.historyAppointments.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.databinding.ItemHistoryBinding

class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemHistoryBinding.bind(view)

    fun bind(appointment: AppointmentResponse) {

        binding.tvName.text  = appointment.user.name + " " + appointment.user.lastName
        binding.tvSpeciality.text = appointment.doctor.speciality
        binding.tvDoctor.text =  appointment.doctor.lastName + " " + appointment.doctor.name
        binding.tvDay.text = appointment.day
        binding.tvHour.text = appointment.hour
    }
}