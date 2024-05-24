package com.mfo.turnosmedicos.ui.scheduleAppointment.searcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.turnosmedicos.data.providers.SpecialityProvider
import com.mfo.turnosmedicos.domain.model.SpecialityInfo
import com.mfo.turnosmedicos.domain.usecase.GetDoctorBySpecialityUseCase
import com.mfo.turnosmedicos.ui.home.MainState
import com.mfo.turnosmedicos.ui.myAppointments.MyAppointmentsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearcherViewModel @Inject constructor(specialityProvider: SpecialityProvider, private val getDoctorBySpecialityUseCase: GetDoctorBySpecialityUseCase): ViewModel() {
    private var _speciality = MutableStateFlow<List<SpecialityInfo>>(emptyList())
    val speciality: StateFlow<List<SpecialityInfo>> = _speciality

    private var _state = MutableStateFlow<SearcherState>(SearcherState.Loading)
    val state: StateFlow<SearcherState> = _state

    init {
        _speciality.value = specialityProvider.getSpeciality()
    }

    fun getDoctorBySpeciality(token: String, speciality: String) {
        viewModelScope.launch {
            _state.value = SearcherState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getDoctorBySpecialityUseCase(token, speciality) }
                if(result != null) {
                    _state.value = SearcherState.Success(result)
                } else {
                    _state.value = SearcherState.Error("Error occurred, Please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = SearcherState.Error(errorMessage)
            }
        }
    }
}