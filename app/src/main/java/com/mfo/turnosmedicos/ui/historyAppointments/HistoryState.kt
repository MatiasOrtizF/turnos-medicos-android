package com.mfo.turnosmedicos.ui.historyAppointments

import com.mfo.turnosmedicos.data.network.response.AppointmentResponse

sealed class HistoryState {
    data object Loading: HistoryState()
    data class Error(val error: String): HistoryState()
    data class Success(val appointment: List<AppointmentResponse>): HistoryState()
}