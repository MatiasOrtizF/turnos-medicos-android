package com.mfo.turnosmedicos.domain.model

class AppointmentRequest(
    val date: String,
    val speciality: String,
    val doctor: DoctorId
) {
    class DoctorId(
        val id: Long
    )
}