package com.mfo.turnosmedicos.ui.scheduleAppointment.searcher

import com.mfo.turnosmedicos.data.network.response.DoctorResponse

sealed class SearcherState {
    data object Loading: SearcherState()
    data class Error(val error: String): SearcherState()
    data class Success(val doctors: List<DoctorResponse>): SearcherState()
}