package com.mfo.turnosmedicos.ui.scheduleAppointment.confirmation

import com.mfo.turnosmedicos.data.network.response.DoctorResponse

sealed class ConfirmationState {
    data object Loading: ConfirmationState()
    data class Error(val error: String): ConfirmationState()
    data class Success(val doctor: DoctorResponse): ConfirmationState()
}