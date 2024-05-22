package com.mfo.turnosmedicos.ui.myAppointments.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.databinding.ItemAppointmentBinding

class MyAppointmentViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemAppointmentBinding.bind(view)

    fun bind(appointment: AppointmentResponse, onItemSelected: (AppointmentResponse) -> Unit) {

        binding.tvName.text = appointment.user.lastName + " " + appointment.user.name
        binding.tvSpeciality.text = appointment.speciality
        binding.tvDoctor.text =  appointment.doctor.lastName + " " + appointment.doctor.name
        binding.tvDay.text = appointment.date.substring(0, 10)
        binding.tvHour.text = appointment.date.substring(appointment.date.length - 5)

        binding.btnCancelAppointment.setOnClickListener { onItemSelected(appointment) }
    }
}