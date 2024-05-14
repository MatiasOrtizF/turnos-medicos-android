package com.mfo.turnosmedicos.data.network.response

import com.google.gson.annotations.SerializedName
import com.mfo.turnosmedicos.domain.model.User

class LoginResponse (@SerializedName ("token") val token: String, @SerializedName("user") val user: User){
    fun toDomain(): LoginResponse {
        return LoginResponse(
            token = token,
            user = user
        )
    }
}