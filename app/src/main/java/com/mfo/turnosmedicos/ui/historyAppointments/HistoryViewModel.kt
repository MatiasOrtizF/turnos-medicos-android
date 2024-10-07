package com.mfo.turnosmedicos.ui.historyAppointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.turnosmedicos.data.network.Constants.ERROR_NETWORK_GENERAL
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val getHistoryUseCase: GetHistoryUseCase): ViewModel() {
    private var _appointment = MutableStateFlow<List<AppointmentResponse>>(emptyList())
    val appointment: StateFlow<List<AppointmentResponse>> = _appointment

    private var _state = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val state: StateFlow<HistoryState> = _state

    fun getHistory(token: String) {
        viewModelScope.launch {
            _state.value = HistoryState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getHistoryUseCase(token) }
                if(result != null) {
                    _appointment.value = result
                    _state.value = HistoryState.Success(result.toMutableList())
                } else {
                    _state.value =
                        HistoryState.Error(ERROR_NETWORK_GENERAL)
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = HistoryState.Error(errorMessage)
            }
        }
    }
}