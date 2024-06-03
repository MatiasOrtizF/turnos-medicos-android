package com.mfo.turnosmedicos.domain.model

data class AppointmentRequest(
    val day: String?,
    val hour: String?,
    val doctor: DoctorId
) {
    data class DoctorId(
        val id: Long?
    )
}