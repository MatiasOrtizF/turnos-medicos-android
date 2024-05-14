package com.mfo.turnosmedicos.data.network

import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.domain.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface TurnosMedicosApiService {

    @POST("login")
    suspend fun authenticationUser(@Body loginRequest: LoginRequest): LoginResponse
}