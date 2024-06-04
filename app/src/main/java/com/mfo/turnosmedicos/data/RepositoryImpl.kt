package com.mfo.turnosmedicos.data

import android.util.Log
import com.mfo.turnosmedicos.data.network.TurnosMedicosApiService
import com.mfo.turnosmedicos.data.network.response.AppointmentAvailableResponse
import com.mfo.turnosmedicos.data.network.response.AppointmentResponse
import com.mfo.turnosmedicos.data.network.response.DoctorResponse
import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.data.network.response.UserResponse
import com.mfo.turnosmedicos.domain.Repository
import com.mfo.turnosmedicos.domain.model.AppointmentRequest
import com.mfo.turnosmedicos.domain.model.LoginRequest
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: TurnosMedicosApiService): Repository {

    // login
    override suspend fun authenticationUser(loginRequest: LoginRequest): LoginResponse? {
        runCatching { apiService.authenticationUser(loginRequest) }
            .onSuccess { return it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    // user
    override suspend fun getUserInfo(token: String): UserResponse? {
        runCatching { apiService.getUserInfo(token)}
            .onSuccess { return it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    // Doctor

    override suspend fun getDoctorBySpeciality(authorization: String, speciality: String): List<DoctorResponse>? {
        runCatching {
            val appointments = apiService.getDoctorBySpeciality(authorization, speciality)
            appointments.map {
                it.toDomain()
            }
        }
            .onSuccess { appointments -> return appointments }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    override suspend fun getDoctorById(token: String, id: Long): DoctorResponse? {
        runCatching { apiService.getDoctorById(token, id)}
            .onSuccess { return it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    // appointment
    override suspend fun addAppointment(token: String, appointmentRequest: AppointmentRequest): AppointmentResponse? {
        runCatching { apiService.addAppointment(token, appointmentRequest) }
            .onSuccess { it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    override suspend fun getAllAppointment(authorization: String): List<AppointmentResponse>? {
        runCatching {
            val appointments = apiService.getAllAppointment(authorization)
            appointments.map {
                it.toDomain()
            }
        }
            .onSuccess { appointments -> return appointments }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    override suspend fun cancelAppointment(authorization: String, id: Long): Boolean? {
        return runCatching {
            val result = apiService.cancelAppointment(authorization, id)
            result["deleted"] ?: false
        }.onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }.getOrNull()
    }

    override suspend fun getAppointmentAvailable(token: String, id: Long): AppointmentAvailableResponse? {
        runCatching { apiService.getAppointmentAvailable(token, id)}
            .onSuccess { return it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }
}