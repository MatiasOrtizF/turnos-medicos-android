package com.mfo.turnosmedicos.data.providers

import com.mfo.turnosmedicos.domain.model.SpecialityInfo
import javax.inject.Inject

class SpecialityProvider @Inject constructor() {
    fun getSpeciality(): List<SpecialityInfo> {
        return listOf(
            SpecialityInfo.Cardiology,
            SpecialityInfo.Dermatology,
            SpecialityInfo.Diabetology,
            SpecialityInfo.Hepatology,
            SpecialityInfo.GeneralMedicine,
            SpecialityInfo.Infectology,
            SpecialityInfo.InternalMedicine,
            SpecialityInfo.Neurology,
            SpecialityInfo.Nursing,
            SpecialityInfo.Nutrition,
            SpecialityInfo.Ophthalmology,
            SpecialityInfo.Orthopedics,
            SpecialityInfo.Pulmonology,
            SpecialityInfo.SpeechTherapy,
            SpecialityInfo.Traumatology
        )
    }
}