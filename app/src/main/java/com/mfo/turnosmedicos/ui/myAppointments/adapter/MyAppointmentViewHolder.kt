package com.mfo.turnosmedicos.ui.myAppointments.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.databinding.ItemMyAppointmentBinding

class MyAppointmentViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemMyAppointmentBinding.bind(view)

    fun bind(appointment: AppointmentResponse, onItemSelected: (Long, Int) -> Unit) {

        binding.tvName.text = appointment.user.lastName + " " + appointment.user.name
        binding.tvSpeciality.text = appointment.doctor.speciality
        binding.tvDoctor.text =  appointment.doctor.lastName + " " + appointment.doctor.name
        binding.tvDay.text = appointment.day
        binding.tvHour.text = appointment.hour

        binding.btnCancelAppointment.setOnClickListener { onItemSelected(appointment.id, adapterPosition) }
    }
}