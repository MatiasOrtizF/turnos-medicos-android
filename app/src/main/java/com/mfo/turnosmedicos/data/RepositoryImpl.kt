package com.mfo.turnosmedicos.data

import android.util.Log
import com.mfo.turnosmedicos.data.network.TurnosMedicosApiService
import com.mfo.turnosmedicos.data.network.response.LoginResponse
import com.mfo.turnosmedicos.data.network.response.UserResponse
import com.mfo.turnosmedicos.domain.Repository
import com.mfo.turnosmedicos.domain.model.LoginRequest
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
}