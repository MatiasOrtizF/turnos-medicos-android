package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName

class DoctorResponse (@SerializedName("name") val name: String, @SerializedName("lastName") val lastName: String) {
    fun toDomain(): DoctorResponse {
        return DoctorResponse(
            name = name,
            lastName = lastName,
        )
    }
}