package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName
import com.mfo.turnosmedicos.domain.model.Doctor
import com.mfo.turnosmedicos.domain.model.User

class AppointmentResponse (
    @SerializedName ("id") val id: Long,
    @SerializedName ("user") val user: User,
    @SerializedName ("day") val day: String,
    @SerializedName ("hour") val hour: String,
    @SerializedName ("doctor") val doctor: Doctor,
){
    fun toDomain(): AppointmentResponse {
        return AppointmentResponse(
            id = id,
            user = user,
            day = day,
            hour = hour,
            doctor = doctor
        )
    }
}