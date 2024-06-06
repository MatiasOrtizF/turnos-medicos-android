package com.mfo.turnosmedicos.domain

import com.mfo.turnosmedicos.data.network.response.AppointmentAvailableResponse
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.data.network.response.DoctorResponse
import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.data.network.response.UserResponse
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.domain.model.LoginRequest
import org.json.JSONObject

interface Repository {
    suspend fun authenticationUser(loginRequest: LoginRequest): LoginResponse?

    // user
    suspend fun getUserInfo(token: String): UserResponse?

    //doctor
    suspend fun getDoctorBySpeciality(authorization: String, speciality: String): List<DoctorResponse>?
    suspend fun getDoctorById(authorization: String, id: Long): DoctorResponse?

    // appointment
    suspend fun addAppointment(authorization: String, appointmentRequest: AppointmentRequest): AppointmentResponse?
    suspend fun getAllAppointment(authorization: String): List<AppointmentResponse>?
    suspend fun cancelAppointment(authorization: String, id: Long): Boolean?
    suspend fun getAppointmentAvailable(authorization: String, id: Long, dayNumber: Int): AppointmentAvailableResponse?

    // history
    suspend fun getHistory(authorization: String): List<AppointmentResponse>?
}