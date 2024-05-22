package com.mfo.turnosmedicos.ui.myAppointments

import com.mfo.turnosmedicos.data.network.response.AppointmentResponse

sealed class MyAppointmentsState {
    data object Loading: MyAppointmentsState()
    data class Error(val error: String): MyAppointmentsState()
    data class Success(val myAppointment: MutableList<AppointmentResponse>, val message: String? = null): MyAppointmentsState()
}