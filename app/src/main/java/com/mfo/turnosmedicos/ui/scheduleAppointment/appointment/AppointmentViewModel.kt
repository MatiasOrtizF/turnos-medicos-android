package com.mfo.turnosmedicos.ui.scheduleAppointment.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.turnosmedicos.data.network.response.AppointmentAvailableResponse
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.domain.usecase.GetAppointmentAvailableUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(private val getAppointmentAvailableUseCase: GetAppointmentAvailableUseCase): ViewModel() {
    private var _hour = MutableStateFlow<List<String>>(emptyList())
    val hour: StateFlow<List<String>> = _hour

    private var _state = MutableStateFlow<AppointmentState>(AppointmentState.Loading)
    val state: StateFlow<AppointmentState> = _state

    fun getAppointmentAvailable(token: String, id: Long, dayNumber: Int) {
        viewModelScope.launch {
            _state.value = AppointmentState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getAppointmentAvailableUseCase(token, id, dayNumber) }
                if(result != null) {
                    _hour.value = result.hour
                    _state.value = AppointmentState.Success(result)
                } else {
                    _state.value = AppointmentState.Error("Error occurred, Please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = AppointmentState.Error(errorMessage)
            }
        }
    }
}