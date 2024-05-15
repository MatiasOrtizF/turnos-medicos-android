package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName

class UserResponse (@SerializedName("name") val name: String, @SerializedName("lastName") val lastName: String, @SerializedName("dni") val dni: Int, @SerializedName("email") val email: String) {
    fun toDomain(): UserResponse {
        return UserResponse(
            name = name,
            lastName = lastName,
            dni = dni,
            email = email
        )
    }
}