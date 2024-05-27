package com.mfo.turnosmedicos.ui.myAppointments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.turnosmedicos.R
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse

class MyAppointmentAdapter(private var appointmentList: MutableList<AppointmentResponse> = mutableListOf(), private val onItemSelected:(Long, Int) -> Unit): RecyclerView.Adapter<MyAppointmentViewHolder>() {
    fun updateList(list: MutableList<AppointmentResponse>) {
        appointmentList = list
        notifyDataSetChanged()
    }

    fun onDeleteItem(position: Int) {
        appointmentList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAppointmentViewHolder {
        return MyAppointmentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_appointment, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    override fun onBindViewHolder(holder: MyAppointmentViewHolder, position: Int) {
        holder.bind(appointmentList[position], onItemSelected)
    }

}