package com.mfo.turnosmedicos.data.network

import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.data.network.response.UserResponse
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.domain.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TurnosMedicosApiService {

    @POST("login")
    suspend fun authenticationUser(@Body loginRequest: LoginRequest): LoginResponse

    @GET("user")
    suspend fun getUserInfo(@Query ("token") token: String): UserResponse

    // Appointment
    @POST("appointment")
    suspend fun addAppointment(
        @Header ("Authorization") authorization: String,
        @Body appointmentRequest: AppointmentRequest
    ): AppointmentResponse

    @GET("appointment")
    suspend fun getAllAppointment(@Header ("Authorization") authorization: String): List<AppointmentResponse>

    @DELETE("appointment/{id}")
    suspend fun cancelFavorite(
        @Header ("Authorization") authorization: String,
        @Path ("id") id: Long
    ): Boolean
}