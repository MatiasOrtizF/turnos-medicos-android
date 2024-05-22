package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName
import com.mfo.turnosmedicos.domain.model.Doctor
import com.mfo.turnosmedicos.domain.model.User

class AppointmentResponse (
    @SerializedName ("id") val id: Long,
    @SerializedName ("date") val date: String,
    @SerializedName ("user") val user: User,
    @SerializedName ("speciality") val speciality: String,
    @SerializedName ("doctor") val doctor: Doctor,
){
    fun toDomain(): AppointmentResponse {
        return AppointmentResponse(
            id = id,
            date = date,
            user = user,
            speciality = speciality,
            doctor = doctor
        )
    }
}