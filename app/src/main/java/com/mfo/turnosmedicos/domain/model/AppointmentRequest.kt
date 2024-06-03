package com.mfo.turnosmedicos.domain.model

class AppointmentRequest(
    val day: String?,
    val hour: String?,
    val doctor: DoctorId
) {
    class DoctorId(
        val id: Long?
    )
}