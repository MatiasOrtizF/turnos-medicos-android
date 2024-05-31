package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName

class DoctorResponse (@SerializedName("id") val id: Long, @SerializedName("name") val name: String, @SerializedName("lastName") val lastName: String) {
    fun toDomain(): DoctorResponse {
        return DoctorResponse(
            id = id,
            name = name,
            lastName = lastName
        )
    }
}