package com.mfo.turnosmedicos.ui.scheduleAppointment.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.domain.usecase.GetDoctorByIdUseCase
import com.mfo.turnosmedicos.domain.usecase.PostAppointmentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ConfirmationViewModel @Inject constructor(private val getDoctorByIdUseCase: GetDoctorByIdUseCase, private val postAppointmentUseCase: PostAppointmentUseCase): ViewModel() {
    private var _state = MutableStateFlow<ConfirmationState>(ConfirmationState.Loading)
    val state: StateFlow<ConfirmationState> = _state

    fun getDoctorById(token: String, id: Long) {
        viewModelScope.launch {
            _state.value = ConfirmationState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getDoctorByIdUseCase(token, id) }
                if(result != null) {
                    _state.value = ConfirmationState.Success(result)
                } else {
                    _state.value = ConfirmationState.Error("Error occurred, Please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = ConfirmationState.Error(errorMessage)
            }
        }
    }

    fun addAppointment(token: String, appointmentRequest: AppointmentRequest) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { postAppointmentUseCase(token, appointmentRequest) }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = ConfirmationState.Error(errorMessage)
            }
        }
    }
}