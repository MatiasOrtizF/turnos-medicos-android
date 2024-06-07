package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName

class UserResponse (@SerializedName("name") val name: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("dni") val dni: Int,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: Int,
    @SerializedName("cellphone") val cellphone: Int
) {
    fun toDomain(): UserResponse {
        return UserResponse(
            name = name,
            lastName = lastName,
            dni = dni,
            email = email,
            phone = phone,
            cellphone = cellphone
        )
    }
}