package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName

class AppointmentAvailableResponse (
    @SerializedName("hour") val hour: List<String>,
    @SerializedName("day") val day: String,
) {
    fun toDomain(): AppointmentAvailableResponse {
        return AppointmentAvailableResponse(
            hour = hour,
            day = day
        )
    }
}