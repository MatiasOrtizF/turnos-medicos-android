package com.mfo.turnosmedicos.ui.scheduleAppointment.appointment

import com.mfo.turnosmedicos.data.network.response.AppointmentAvailableResponse

sealed class AppointmentState {
    data object Loading: AppointmentState()
    data class Error(val error: String): AppointmentState()
    data class Success(val appointmentAvailable: AppointmentAvailableResponse): AppointmentState()
}