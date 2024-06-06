package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName

class AppointmentAvailableResponse (
    @SerializedName("hour") val hour: List<String>,
    @SerializedName("day") val day: String,
    @SerializedName("dayNumber") val dayNumber: Int
) {
    fun toDomain(): AppointmentAvailableResponse {
        return AppointmentAvailableResponse(
            hour = hour,
            day = day,
            dayNumber = dayNumber
        )
    }
}