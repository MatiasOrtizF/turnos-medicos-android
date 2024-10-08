package com.mfo.turnosmedicos.ui.myAppointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.turnosmedicos.data.network.Constants.ERROR_NETWORK_GENERAL
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.domain.usecase.DeleteAppointmentUseCase
import com.mfo.turnosmedicos.domain.usecase.GetAllAppointmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyAppointmentsViewModel @Inject constructor(
    private val getAllAppointmentUseCase: GetAllAppointmentUseCase,
    val deleteAppointmentUseCase: DeleteAppointmentUseCase
): ViewModel() {
    private var _appointment = MutableStateFlow<List<AppointmentResponse>>(emptyList())
    val appointment: StateFlow<List<AppointmentResponse>> = _appointment

    private var _state = MutableStateFlow<MyAppointmentsState>(MyAppointmentsState.Loading)
    val state: StateFlow<MyAppointmentsState> = _state

    fun getAllMyAppointment(authorization: String) {
        viewModelScope.launch {
            _state.value = MyAppointmentsState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getAllAppointmentUseCase(authorization) }
                if(result != null) {
                    _appointment.value = result
                    _state.value = MyAppointmentsState.Success(result.toMutableList())
                } else {
                    _state.value = MyAppointmentsState.Error(ERROR_NETWORK_GENERAL)
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = MyAppointmentsState.Error(errorMessage)
            }
        }
    }

    suspend fun cancelAppointment(authorization: String, id: Long): Boolean {
        _state.value = MyAppointmentsState.Loading
        return try {
            val result = withContext(Dispatchers.IO) { deleteAppointmentUseCase(authorization, id) }
            if (result != null) {
                _state.value = MyAppointmentsState.CancelSuccess(result)
                true
            } else {
                _state.value = MyAppointmentsState.Error(ERROR_NETWORK_GENERAL)
                false
            }
            } catch (e: Exception) {
            val errorMessage: String = e.message.toString()
            _state.value = MyAppointmentsState.Error(errorMessage)
            false
        }
    }
}