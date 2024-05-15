package com.mfo.turnosmedicos.data.network

import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.data.network.response.UserResponse
import com.mfo.turnosmedicos.domain.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TurnosMedicosApiService {

    @POST("login")
    suspend fun authenticationUser(@Body loginRequest: LoginRequest): LoginResponse

    @GET("user")
    suspend fun getUserInfo(@Query ("token") token: String): UserResponse
}