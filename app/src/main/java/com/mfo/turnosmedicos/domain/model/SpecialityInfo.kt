package com.mfo.turnosmedicos.domain.model

import com.mfo.turnosmedicos.R

sealed class SpecialityInfo(val name: Int) {
    object Cardiology : SpecialityInfo(R.string.tv_cardiology)

    object InternalMedicine : SpecialityInfo(R.string.tv_internal_medicine)

    object Dermatology : SpecialityInfo(R.string.tv_dermatology)

    object Diabetology : SpecialityInfo(R.string.tv_diabetology)

    object Nursing : SpecialityInfo(R.string.tv_nursing)

    object SpeechTherapy : SpecialityInfo(R.string.tv_speech_therapy)

    object Hepatology : SpecialityInfo(R.string.tv_hepatology)

    object Infectology : SpecialityInfo(R.string.tv_infectology)

    object GeneralMedicine : SpecialityInfo(R.string.tv_general_medicine)

    object Pulmonology : SpecialityInfo(R.string.tv_pulmonology)

    object Neurology : SpecialityInfo(R.string.tv_neurology)

    object Nutrition : SpecialityInfo(R.string.tv_nutrition)

    object Ophthalmology : SpecialityInfo(R.string.tv_ophthalmology)

    object Orthopedics : SpecialityInfo(R.string.tv_orthopedics)

    object Traumatology : SpecialityInfo(R.string.tv_traumatology)
}