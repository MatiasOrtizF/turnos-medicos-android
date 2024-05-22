package com.mfo.turnosmedicos.ui.scheduleAppointment.searcher

import androidx.lifecycle.ViewModel
import com.mfo.turnosmedicos.data.providers.SpecialityProvider
import com.mfo.turnosmedicos.domain.model.SpecialityInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearcherViewModel @Inject constructor(specialityProvider: SpecialityProvider): ViewModel() {
    private var _speciality = MutableStateFlow<List<SpecialityInfo>>(emptyList())
    val speciality: StateFlow<List<SpecialityInfo>> = _speciality

    init {
        _speciality.value = specialityProvider.getSpeciality()
    }
}