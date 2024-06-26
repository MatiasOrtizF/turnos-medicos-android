package com.mfo.turnosmedicos.data.network

import com.mfo.turnosmedicos.data.network.response.AppointmentAvailableResponse
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.data.network.response.DoctorResponse
import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.data.network.response.UserResponse
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.domain.model.LoginRequest
import org.json.JSONObject
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

    // User
    @GET("user")
    suspend fun getUserInfo(@Query ("token") token: String): UserResponse

    // Doctor
    @GET("doctor/bySpeciality/{speciality}")
    suspend fun getDoctorBySpeciality(
        @Header ("Authorization") authorization: String,
        @Path ("speciality") speciality: String
    ): List<DoctorResponse>

    @GET("doctor/{id}")
    suspend fun getDoctorById(
        @Header ("Authorization") authorization: String,
        @Path ("id") id: Long
    ): DoctorResponse

    // Appointment
    @POST("appointment")
    suspend fun addAppointment(
        @Header ("Authorization") authorization: String,
        @Body appointmentRequest: AppointmentRequest
    ): AppointmentResponse

    @GET("appointment")
    suspend fun getAllAppointment(@Header ("Authorization") authorization: String): List<AppointmentResponse>

    @DELETE("appointment/{id}")
    suspend fun cancelAppointment(
        @Header ("Authorization") authorization: String,
        @Path ("id") id: Long
    ): Map<String, Boolean>

    @GET("appointment/{id}")
    suspend fun getAppointmentAvailable(
        @Header ("Authorization") authorization: String,
        @Path ("id") id: Long,
        @Query ("dayNumber") dayNumber: Int
    ): AppointmentAvailableResponse

    // History
    @GET("history")
    suspend fun getHistory(@Header ("Authorization") authorization: String): List<AppointmentResponse>
}